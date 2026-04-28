package com.pedroheing.shoppingcart.checkout;

import com.pedroheing.shoppingcart.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class EmptyCartException extends DomainException {
    public EmptyCartException(String userId) {
        super("Cart is empty for user: " + userId);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String errorCode() {
        return "EMPTY_CART";
    }
}