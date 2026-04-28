package com.pedroheing.shoppingcart.checkout;

import com.pedroheing.shoppingcart.common.exception.DomainException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class PriceChangedException extends DomainException {
    private final String productId;
    private final BigDecimal expectedPrice;
    private final BigDecimal currentPrice;

    public PriceChangedException(String productId, BigDecimal expectedPrice, BigDecimal currentPrice) {
        super(String.format("Price changed for product %s: expected %s, current %s",
                productId, expectedPrice, currentPrice));
        this.productId = productId;
        this.expectedPrice = expectedPrice;
        this.currentPrice = currentPrice;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String errorCode() {
        return "PRICE_CHANGED";
    }

    @Override
    public Map<String, Object> details() {
        return Map.of(
                "productId", productId,
                "expectedPrice", expectedPrice,
                "currentPrice", currentPrice
        );
    }
}