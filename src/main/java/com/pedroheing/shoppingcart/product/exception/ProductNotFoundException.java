package com.pedroheing.shoppingcart.product.exception;

import com.pedroheing.shoppingcart.common.exception.DomainException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class ProductNotFoundException extends DomainException {
    private final String productId;

    public ProductNotFoundException(String productId) {
        super(String.format("Product not found: %s", productId));
        this.productId = productId;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String errorCode() {
        return "NOT_FOUND";
    }

    @Override
    public Map<String, Object> details() {
        return Map.of(
                "productId", productId
        );
    }
}