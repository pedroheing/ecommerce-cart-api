package com.pedroheing.shoppingcart.product;

import com.pedroheing.shoppingcart.product.dto.CreateProductInput;
import com.pedroheing.shoppingcart.product.dto.UpdateProductInput;
import com.pedroheing.shoppingcart.product.exception.InsufficientStockException;
import com.pedroheing.shoppingcart.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseProductServiceTest {

    @Mock ProductRepository productRepository;
    @InjectMocks DatabaseProductService service;

    private Product product() {
        return Product.builder().name("Wireless Headphones").stock(10).price(new BigDecimal("99.99")).build();
    }

    @Test
    void findById_productExists_returnsProduct() {
        var productId = "id-1";
        var p = product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(p));

        assertThat(service.findById(productId)).isEqualTo(p);
    }

    @Test
    void findById_productNotFound_throwsProductNotFoundException() {
        var productId = "id-1";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(productId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void create_validInput_savesAndReturnsProduct() {
        var input = new CreateProductInput("Wireless Headphones", new BigDecimal("99.99"), 10);
        var saved = product();
        when(productRepository.save(any())).thenReturn(saved);

        var result = service.create(input);

        assertThat(result).isEqualTo(saved);
        verify(productRepository).save(any());
    }

    @Test
    void update_productExists_appliesNameChange() {
        var productId = "id-1";
        var p = product();
        var newName = "Pro Headphones";
        var input = new UpdateProductInput(Optional.of(newName), Optional.empty(), Optional.empty());
        when(productRepository.findById(productId)).thenReturn(Optional.of(p));
        when(productRepository.save(p)).thenReturn(p);

        var result = service.update(productId, input);

        assertThat(result.getName()).isEqualTo(newName);
    }

    @Test
    void update_productNotFound_throwsProductNotFoundException() {
        var productId = "id-1";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(productId,
                new UpdateProductInput(Optional.empty(), Optional.empty(), Optional.empty())))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void delete_productExists_deletesById() {
        var productId = "id-1";
        when(productRepository.existsById(productId)).thenReturn(true);

        service.delete(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void delete_productNotFound_throwsProductNotFoundException() {
        var productId = "id-1";
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(productId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void decrementStock_success_noExceptionThrown() {
        var productId = "id-1";
        var amount = 3;
        when(productRepository.decrementStock(productId, amount)).thenReturn(1);

        assertThatCode(() -> service.decrementStock(productId, amount)).doesNotThrowAnyException();
    }

    @Test
    void decrementStock_insufficientStock_throwsInsufficientStockException() {
        var productId = "id-1";
        var amount = 3;
        when(productRepository.decrementStock(productId, amount)).thenReturn(0);
        when(productRepository.existsById(productId)).thenReturn(true);

        assertThatThrownBy(() -> service.decrementStock(productId, amount))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void decrementStock_productNotFound_throwsProductNotFoundException() {
        var productId = "id-1";
        var amount = 3;
        when(productRepository.decrementStock(productId, amount)).thenReturn(0);
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThatThrownBy(() -> service.decrementStock(productId, amount))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void decrementStock_zeroAmount_throwsIllegalArgument() {
        var productId = "id-1";

        assertThatThrownBy(() -> service.decrementStock(productId, 0))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(productRepository);
    }
}
