package com.pedroheing.shoppingcart.checkout;

import com.pedroheing.shoppingcart.order.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        String id,
        String productId,
        String productName,
        BigDecimal unitPrice,
        int amount,
        BigDecimal subtotal
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