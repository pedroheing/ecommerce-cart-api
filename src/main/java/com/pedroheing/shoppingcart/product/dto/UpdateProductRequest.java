package com.pedroheing.shoppingcart.product.dto;

import com.pedroheing.shoppingcart.common.serializer.BigDecimalStringDeserializer;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank String name,
        @NotNull @Digits(integer = 15, fraction = 4) @JsonDeserialize(using = BigDecimalStringDeserializer.class) BigDecimal price
) {}
