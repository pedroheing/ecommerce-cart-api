package com.pedroheing.shoppingcart.product.exception;

import com.pedroheing.shoppingcart.common.exception.DomainException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class InsufficientStockException extends DomainException {
    private final String productId;
    private final int requestedAmount;

    public InsufficientStockException(String productId, int requestedAmount) {
        super(String.format("Insufficient stock for product %s (requested: %d)", productId, requestedAmount));
        this.productId = productId;
        this.requestedAmount = requestedAmount;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String errorCode() {
        return "INSUFFICIENT_STOCK";
    }

    @Override
    public Map<String, Object> details() {
        return Map.of(
                "productId", productId,
                "requestedAmount", requestedAmount
        );
    }
}