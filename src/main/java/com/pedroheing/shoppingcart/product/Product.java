package com.pedroheing.shoppingcart.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Schema(example = "Wireless Headphones")
    @Column(nullable = false)
    private String name;

    @Schema(example = "50")
    @Column(nullable = false)
    private int stock;

    @Schema(example = "149.99")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Builder
    public Product(String name, int stock, BigDecimal price) {
        validateName(name);
        validateStock(stock);
        validatePrice(price);
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changePrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public void changeStock(int amount) {
        validateStock(amount);
        this.stock = amount;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
    }

    private static void validateStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("stock cannot be negative");
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
    }
}
