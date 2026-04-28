package com.pedroheing.shoppingcart.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private BigDecimal subtotal;

    public OrderItem(String productId, String productName, BigDecimal unitPrice, int amount) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId is required");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName is required");
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must be non-negative");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(amount));
    }

    void attachTo(Order order) {
        this.order = order;
    }
}