package com.pedroheing.shoppingcart.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record CartItem(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000") String productId,
        @Schema(example = "Wireless Headphones") String name,
        @Schema(example = "149.99") BigDecimal price,
        @Schema(example = "2") int amount
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