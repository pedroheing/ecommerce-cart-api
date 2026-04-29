package com.pedroheing.shoppingcart.cart;

import com.pedroheing.shoppingcart.cart.dto.PutItemInput;
import com.pedroheing.shoppingcart.product.Product;
import com.pedroheing.shoppingcart.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock CartRepository cartRepository;
    @Mock ProductService productService;
    @InjectMocks CartService cartService;

    private Product product(String name, BigDecimal price) {
        return Product.builder().name(name).stock(10).price(price).build();
    }

    @Test
    void putItem_productExists_savesItemAndReturnsIt() {
        var price = new BigDecimal("99.99");
        var product = product("Wireless Headphones", price);
        var input = new PutItemInput("user-1", "prod-1", 2);
        when(productService.findById(input.productId())).thenReturn(product);

        var result = cartService.putItem(input);

        assertThat(result.productId()).isEqualTo(input.productId());
        assertThat(result.name()).isEqualTo(product.getName());
        assertThat(result.price()).isEqualByComparingTo(product.getPrice());
        assertThat(result.amount()).isEqualTo(input.amount());
        verify(cartRepository).putItem(input.userId(), result);
    }

    @Test
    void removeItem_delegatesToRepository() {
        var userId = "user-1";
        var productId = "prod-1";

        cartService.removeItem(userId, productId);

        verify(cartRepository).removeItem(userId, productId);
    }

    @Test
    void clearCart_delegatesToRepository() {
        var userId = "user-1";

        cartService.clearCart(userId);

        verify(cartRepository).clearCart(userId);
    }

    @Test
    void listItems_returnsItemsFromRepository() {
        var userId = "user-1";
        var items = List.of(new CartItem("prod-1", "Wireless Headphones", BigDecimal.ONE, 1));
        when(cartRepository.listItems(userId)).thenReturn(items);

        var result = cartService.listItems(userId);

        assertThat(result).isEqualTo(items);
    }
}
