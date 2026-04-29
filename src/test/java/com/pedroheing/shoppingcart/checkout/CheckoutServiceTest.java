package com.pedroheing.shoppingcart.checkout;

import com.pedroheing.shoppingcart.cart.CartItem;
import com.pedroheing.shoppingcart.cart.CartService;
import com.pedroheing.shoppingcart.order.Order;
import com.pedroheing.shoppingcart.order.OrderService;
import com.pedroheing.shoppingcart.product.Product;
import com.pedroheing.shoppingcart.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock CartService cartService;
    @Mock ProductService productService;
    @Mock OrderService orderService;
    @InjectMocks CheckoutService checkoutService;

    private CartItem cartItem(String productId, BigDecimal price, int amount) {
        return new CartItem(productId, "Wireless Headphones", price, amount);
    }

    private Product product(BigDecimal price) {
        return Product.builder().name("Wireless Headphones").stock(10).price(price).build();
    }

    @Test
    void checkout_emptyCart_throwsEmptyCartException() {
        var userId = "user-1";
        when(cartService.listItems(userId)).thenReturn(List.of());

        assertThatThrownBy(() -> checkoutService.checkout(userId))
                .isInstanceOf(EmptyCartException.class);

        verifyNoInteractions(productService, orderService);
    }

    @Test
    void checkout_priceChanged_throwsPriceChangedException() {
        var userId = "user-1";
        var item = cartItem("prod-1", new BigDecimal("100.00"), 1);
        when(cartService.listItems(userId)).thenReturn(List.of(item));
        when(productService.findById(item.productId())).thenReturn(product(new BigDecimal("120.00")));

        assertThatThrownBy(() -> checkoutService.checkout(userId))
                .isInstanceOf(PriceChangedException.class);

        verify(productService, never()).decrementStock(any(), anyInt());
    }

    @Test
    void checkout_happyPath_decrementsStockSavesOrderAndClearsCart() {
        var userId = "user-1";
        var price = new BigDecimal("50.00");
        var item = cartItem("prod-1", price, 2);
        var savedOrder = mock(Order.class);

        when(cartService.listItems(userId)).thenReturn(List.of(item));
        when(productService.findById(item.productId())).thenReturn(product(price));
        when(orderService.save(any())).thenReturn(savedOrder);

        var result = checkoutService.checkout(userId);

        assertThat(result).isEqualTo(savedOrder);
        verify(productService).decrementStock(item.productId(), item.amount());
        verify(orderService).save(any());
        verify(cartService).clearCart(userId);
    }

    @Test
    void checkout_multipleItems_validatesAndDecrementsAllItems() {
        var userId = "user-1";
        var price1 = new BigDecimal("100.00");
        var price2 = new BigDecimal("30.00");
        var item1 = cartItem("prod-1", price1, 1);
        var item2 = cartItem("prod-2", price2, 3);
        var savedOrder = mock(Order.class);

        when(cartService.listItems(userId)).thenReturn(List.of(item1, item2));
        when(productService.findById(item1.productId())).thenReturn(product(price1));
        when(productService.findById(item2.productId())).thenReturn(product(price2));
        when(orderService.save(any())).thenReturn(savedOrder);

        checkoutService.checkout(userId);

        verify(productService).decrementStock(item1.productId(), item1.amount());
        verify(productService).decrementStock(item2.productId(), item2.amount());
    }
}
