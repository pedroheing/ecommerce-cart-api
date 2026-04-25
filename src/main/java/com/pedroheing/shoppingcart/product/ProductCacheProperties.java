package com.pedroheing.shoppingcart.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "shoppingcart.product.cache")
public record ProductCacheProperties(
        @NotBlank String keyPrefix,
        @NotNull Duration ttl
) {}