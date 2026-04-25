package com.pedroheing.shoppingcart.cart;
import java.math.BigDecimal;

public record CartItem(
        String productId,
        String name,
        BigDecimal price,
        int amount
) {
    public CartItem {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }

    public CartItem withAmount(int newAmount) {
        return new CartItem(productId, name, price, newAmount);
    }
}