package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.common.exception.NotFoundException;
import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
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
                .price(input.price())
                .build();
        return productRepository.save(product);
    }

    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    @Transactional
    public Product update(String id, UpdateProductInput input) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
        product.setName(input.name());
        product.setPrice(input.price());
        return productRepository.save(product);
    }

    @Transactional
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}
