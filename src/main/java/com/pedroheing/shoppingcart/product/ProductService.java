package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;


public interface ProductService {
    Product create(CreateProductInput input);
    Product findById(String id);
    Product update(String id, UpdateProductInput input);
    void delete(String id);
}
