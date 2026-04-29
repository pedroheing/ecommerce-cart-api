package com.pedroheing.shoppingcart.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        @Schema(example = "7c9e6679-7425-40de-944b-e07fc1f90ae7")
        String id,
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        String userId,
        @Schema(example = "299.98")
        BigDecimal total,
        @Schema(example = "2024-01-15T10:30:00Z")
        Instant createdAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(OrderItemResponse::from)
                        .toList()
        );
    }
}