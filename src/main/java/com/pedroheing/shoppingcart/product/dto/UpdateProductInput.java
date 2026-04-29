package com.pedroheing.shoppingcart.product.dto;

import java.math.BigDecimal;
import java.util.Optional;

public record UpdateProductInput(
        Optional<String> name,
        Optional<BigDecimal> price,
        Optional<Integer> stock
) {}