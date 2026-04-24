package com.pedroheing.shoppingcart.product.dto;

import java.math.BigDecimal;

public record UpdateProductInput(String name, BigDecimal price) {}
