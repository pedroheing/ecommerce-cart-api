package com.pedroheing.shoppingcart.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        String name,
        @Email String email
) {}
