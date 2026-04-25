package com.pedroheing.shoppingcart.cart.dto;

import jakarta.validation.constraints.*;

public record AddToCartRequest(
        @NotBlank String productId,
        @Positive @NotNull Integer amount
) {}
