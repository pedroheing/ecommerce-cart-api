package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.CreateProductRequest;
import com.pedroheing.shoppingcart.product.dto.ProductDTO;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody CreateProductRequest request) {
        var input = new CreateProductInput(request.name(), request.price());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(input));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable String id, @Valid @RequestBody UpdateProductRequest request) {
        var input = new UpdateProductInput(request.name(), request.price());
        return ResponseEntity.ok(productService.update(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
