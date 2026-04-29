package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
import com.pedroheing.shoppingcart.product.exception.InsufficientStockException;
import com.pedroheing.shoppingcart.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service("databaseProductService")
public class DatabaseProductService implements ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(CreateProductInput input) {
        Product product = Product.builder()
                .name(input.name())
                .stock(input.stock())
                .price(input.price())
                .build();
        return productRepository.save(product);
    }

    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public Product update(String id, UpdateProductInput input) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        input.name().ifPresent(product::changeName);
        input.price().ifPresent(product::changePrice);
        input.stock().ifPresent(product::restock);
        return productRepository.save(product);
    }

    @Transactional
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    public void decrementStock(String productId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        int updated = productRepository.decrementStock(productId, amount);
        if (updated == 0) {
            if (!productRepository.existsById(productId)) {
                throw new ProductNotFoundException(productId);
            }
            throw new InsufficientStockException(productId, amount);
        }
    }
}
