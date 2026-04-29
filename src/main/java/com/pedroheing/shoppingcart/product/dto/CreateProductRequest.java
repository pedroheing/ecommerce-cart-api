package com.pedroheing.shoppingcart.product.dto;

import com.pedroheing.shoppingcart.common.serializer.BigDecimalStringDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

public record CreateProductRequest(
        @Schema(example = "Wireless Headphones") @NotBlank String name,
        @Schema(example = "149.99") @NotNull @Digits(integer = 15, fraction = 4) @JsonDeserialize(using = BigDecimalStringDeserializer.class) BigDecimal price,
        @Schema(example = "50") @Positive @NotNull Integer stock
) {}
