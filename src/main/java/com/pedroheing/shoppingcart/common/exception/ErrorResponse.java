package com.pedroheing.shoppingcart.common.exception;

import java.util.Map;

public record ErrorResponse(
        String error,
        String message,
        Map<String, Object> details
) {}