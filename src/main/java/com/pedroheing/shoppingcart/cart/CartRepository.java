package com.pedroheing.shoppingcart.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CartRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "cart";

    public void putItem(String userId, CartItem item) {
        var pk = this.buildPkKey(userId);
        var sk = this.buildSkKey(item.productId());
        var updateRequest = UpdateItemRequest.builder()
                .tableName(this.tableName)
                .key(Map.of(
                "PK", AttributeValue.fromS(pk),
                "SK", AttributeValue.fromS(sk)
                ))
                .updateExpression("SET amount = :amount, #name = :name, price = :price")
                .expressionAttributeNames(Map.of("#name", "name"))
                .expressionAttributeValues(Map.of(
                        ":amount", AttributeValue.fromN(String.valueOf(item.amount())),
                        ":name", AttributeValue.fromS(item.name()),
                        ":price", AttributeValue.fromN(item.price().toString())
                ))
                .build();
        dynamoDbClient.updateItem(updateRequest);
    }

    public void removeItem(String userId, String productId) {
        var pk = this.buildPkKey(userId);
        var sk = this.buildSkKey(productId);
        var deleteRequest = DeleteItemRequest.builder()
                .tableName(this.tableName)
                .key(Map.of(
                "PK", AttributeValue.fromS(pk),
                "SK", AttributeValue.fromS(sk)
                ))
                .build();
        dynamoDbClient.deleteItem(deleteRequest);
    }

    public List<CartItem> listItems(String userId) {
        var pk = this.buildPkKey(userId);
        var queryRequest = QueryRequest.builder()
                .tableName(this.tableName)
                .keyConditionExpression("PK = :PK")
                .expressionAttributeValues(Map.of(":PK", AttributeValue.fromS(pk)))
                .build();
        QueryResponse queryResponse = this.dynamoDbClient.query(queryRequest);
        return queryResponse.items().stream()
                .map(this::toCartItem)
                .toList();
    }

    public void clearCart(String userId) {
        var pk = this.buildPkKey(userId);

        Map<String, AttributeValue> lastKey = null;
        List<Map<String, AttributeValue>> itemsToDelete = new ArrayList<>();
        do {
            var request = QueryRequest.builder()
                    .tableName(tableName)
                    .keyConditionExpression("PK = :PK")
                    .expressionAttributeValues(Map.of(":PK", AttributeValue.fromS(pk)))
                    .projectionExpression("PK, SK")
                    .exclusiveStartKey(lastKey)
                    .build();
            var response = dynamoDbClient.query(request);
            itemsToDelete.addAll(response.items());
            lastKey = response.lastEvaluatedKey();
        } while (!lastKey.isEmpty());

        if (itemsToDelete.isEmpty()) return;

        List<WriteRequest> deleteRequests = new ArrayList<>();
        for (var item : itemsToDelete) {
            var deleteRequest = DeleteRequest.builder().key(item).build();
            deleteRequests.add(WriteRequest.builder().deleteRequest(deleteRequest).build());
        }

        int dynamoBatchLimit = 25;
        for (int start = 0; start < deleteRequests.size(); start += dynamoBatchLimit) {
            var end = Math.min(start + dynamoBatchLimit, deleteRequests.size());
            var batch = deleteRequests.subList(start, end);
            var batchRequest = BatchWriteItemRequest.builder()
                    .requestItems(Map.of(tableName, batch))
                    .build();
            // batchWriteItem may return UnprocessedItems
            // a production implementation would retry, but it is out of scope for this project
            dynamoDbClient.batchWriteItem(batchRequest);
        }
    }

    private String buildPkKey(String userId) {
        return "cart#" + userId;
    }

    private String buildSkKey(String productId) {
        return "prod#" + productId;
    }

    private CartItem toCartItem(Map<String, AttributeValue> item) {
        return new CartItem(
            item.get("SK").s().replace("prod#", ""),
            item.get("name").s(),
            new BigDecimal(item.get("price").n()),
            Integer.parseInt(item.get("amount").n())
        );
    }
}
