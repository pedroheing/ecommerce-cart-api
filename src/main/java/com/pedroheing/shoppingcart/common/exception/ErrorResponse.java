package com.pedroheing.shoppingcart.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record ErrorResponse(
        @Schema(example = "PRODUCT_NOT_FOUND") String error,
        @Schema(example = "Product with id '550e8400-e29b-41d4-a716-446655440000' not found") String message,
        Map<String, Object> details
) {}