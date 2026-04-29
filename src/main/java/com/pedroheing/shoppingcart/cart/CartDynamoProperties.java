package com.pedroheing.shoppingcart.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "shoppingcart.cart.dynamo")
public record CartDynamoProperties(
        @NotBlank String tableName,
        @Min(1) int batchSize,
        @NotNull Duration ttl
) {}