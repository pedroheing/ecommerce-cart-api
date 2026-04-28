package com.pedroheing.shoppingcart.product;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.stock = p.stock - :amount " +
            "WHERE p.id = :productId AND p.stock >= :amount")
    int decrementStock(@Param("productId") String productId, @Param("amount") int amount);
}
