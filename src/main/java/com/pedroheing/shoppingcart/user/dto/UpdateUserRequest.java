package com.pedroheing.shoppingcart.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @Schema(example = "Alice Johnson") @NotBlank String name,
        @Schema(example = "alice.johnson@example.com") @NotBlank @Email String email
) {}
