package com.pedroheing.shoppingcart.user.dto;

import java.util.Optional;

public record UpdateUserInput(
        Optional<String> name,
        Optional<String> email
) {}