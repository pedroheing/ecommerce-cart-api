package com.pedroheing.shoppingcart.product.dto;

import java.math.BigDecimal;

public record CreateProductInput(String name, BigDecimal price) {}
