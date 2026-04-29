package com.pedroheing.shoppingcart.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AddToCartRequest(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000") @NotBlank String productId,
        @Schema(example = "2") @Positive @NotNull Integer amount
) {}
