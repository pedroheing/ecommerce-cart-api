package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service("cachedProductService")
public class RedisProductService implements  ProductService {

    private final ProductService productService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductCacheProperties cacheProperties;

    public RedisProductService(
            @Qualifier("databaseProductService") ProductService productService,
            RedisTemplate<String, Object> redisTemplate,
            ProductCacheProperties cacheProperties
    ) {
        this.productService = productService;
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }

    public String buildKey(String productId) {
        return cacheProperties.keyPrefix()  + productId;
    }

    @Override
    public Product create(CreateProductInput input) {
        Product newProduct = this.productService.create(input);
        redisTemplate.opsForValue().set(this.buildKey(newProduct.getId()), newProduct, cacheProperties.ttl());
        return newProduct;
    }

    @Override
    public Product findById(String id) {
        Product product = (Product) redisTemplate.opsForValue().get(this.buildKey(id));
        if (product != null) {
            return product;
        }
        product = this.productService.findById(id);
        redisTemplate.opsForValue().set(this.buildKey(id), product, cacheProperties.ttl());
        return product;
    }

    @Override
    public Product update(String id, UpdateProductInput input) {
        Product newProduct = this.productService.update(id, input);
        redisTemplate.opsForValue().set(this.buildKey(newProduct.getId()), newProduct, cacheProperties.ttl());
        return newProduct;
    }

    @Override
    public void delete(String id) {
        this.productService.delete(id);
        redisTemplate.delete(this.buildKey(id));
    }
}
