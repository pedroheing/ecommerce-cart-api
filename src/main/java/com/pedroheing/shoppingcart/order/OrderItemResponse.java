package com.pedroheing.shoppingcart.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record OrderItemResponse(
        @Schema(example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890") String id,
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000") String productId,
        @Schema(example = "Wireless Headphones") String productName,
        @Schema(example = "149.99") BigDecimal unitPrice,
        @Schema(example = "2") int amount,
        @Schema(example = "299.98") BigDecimal subtotal
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getAmount(),
                item.getSubtotal()
        );
    }
}