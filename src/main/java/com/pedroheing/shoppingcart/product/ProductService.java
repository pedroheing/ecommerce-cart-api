package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.common.exception.NotFoundException;
import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.ProductDTO;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO create(CreateProductInput input) {
        Product product = Product.builder()
                .name(input.name())
                .price(input.price())
                .build();
        return toDTO(productRepository.save(product));
    }

    public ProductDTO findById(String id) {
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    @Transactional
    public ProductDTO update(String id, UpdateProductInput input) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
        product.setName(input.name());
        product.setPrice(input.price());
        return toDTO(productRepository.save(product));
    }

    @Transactional
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice());
    }
}
