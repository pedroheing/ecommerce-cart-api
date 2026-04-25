package com.pedroheing.shoppingcart.cart.dto;

public record PutItemInput(String userId, String productId, int amount){ }