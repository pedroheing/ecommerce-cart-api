package com.pedroheing.shoppingcart.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "shoppingcart.cart.dynamo")
public record CartDynamoProperties(
        @NotBlank String tableName,
        @Min(1) int batchSize
) {}