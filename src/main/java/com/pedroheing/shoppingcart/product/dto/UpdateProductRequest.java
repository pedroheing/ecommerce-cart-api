package com.pedroheing.shoppingcart.product.dto;

import com.pedroheing.shoppingcart.common.serializer.BigDecimalStringDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @Schema(example = "Wireless Headphones Pro") String name,
        @Schema(example = "199.99") @Digits(integer = 15, fraction = 4)
        @JsonDeserialize(using = BigDecimalStringDeserializer.class)
        BigDecimal price,
        @Schema(example = "100") Integer stock
) {}
