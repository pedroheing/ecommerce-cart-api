package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("writeThroughProductService")
public class WriteThroughProductService implements ProductService {

    private final ProductService databaseProductService;
    private final ProductService cachedProductService;

    public WriteThroughProductService(
            @Qualifier("databaseProductService") ProductService databaseProductService,
            @Qualifier("cachedProductService") ProductService cachedProductService
    ) {
        this.databaseProductService = databaseProductService;
        this.cachedProductService = cachedProductService;
    }

    @Override
    public Product findById(String id) {
        return databaseProductService.findById(id);
    }

    @Override
    public Product create(CreateProductInput input) {
        return cachedProductService.create(input);
    }

    @Override
    public Product update(String id, UpdateProductInput input) {
        return cachedProductService.update(id, input);
    }

    @Override
    public void delete(String id) {
        cachedProductService.delete(id);
    }

    @Override
    public void decrementStock(String productId, int amount) {
        cachedProductService.decrementStock(productId, amount);
    }
}