package com.pedroheing.shoppingcart.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(String userId, List<OrderItem> items) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("order requires at least one item");
        }

        this.userId = userId;
        this.status = OrderStatus.PENDING;
        this.createdAt = Instant.now();
        this.total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        items.forEach(item -> {
            item.attachTo(this);
            this.items.add(item);
        });
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("can only confirm pending orders, was: " + status);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void fail(String reason) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("can only fail pending orders, was: " + status);
        }
        this.status = OrderStatus.FAILED;
    }
}