package com.pedroheing.shoppingcart.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "shoppingcart.dynamo")
public record DynamoProperties(
        @NotBlank String region,
        @NotBlank String endpoint,
        @NotBlank String accessKey,
        @NotBlank String secretKey
) {}