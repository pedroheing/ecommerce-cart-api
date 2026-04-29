package com.pedroheing.shoppingcart.checkout;

import com.pedroheing.shoppingcart.cart.CartItem;
import com.pedroheing.shoppingcart.cart.CartService;
import com.pedroheing.shoppingcart.order.Order;
import com.pedroheing.shoppingcart.order.OrderItem;
import com.pedroheing.shoppingcart.order.OrderService;
import com.pedroheing.shoppingcart.product.Product;
import com.pedroheing.shoppingcart.product.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    private final CartService cartService;
    private final ProductService databaseProductService;
    private final OrderService orderService;

    public CheckoutService(
            CartService cartService,
            @Qualifier("writeThroughProductService") ProductService databaseProductService,
            OrderService orderService
    ) {
        this.cartService = cartService;
        this.databaseProductService = databaseProductService;
        this.orderService = orderService;
    }

    @Transactional
    public Order checkout(String userId) {
        var cartItems = cartService.listItems(userId);
        if (cartItems.isEmpty()) throw new EmptyCartException(userId);

        for (var item : cartItems) {
            var product = databaseProductService.findById(item.productId());
            validatePrice(item, product);
        }

        for (var item : cartItems) {
            databaseProductService.decrementStock(item.productId(), item.amount());
        }

        var orderItems = cartItems.stream()
                .map(this::buildOrderItem)
                .toList();
        Order order = new Order(userId, orderItems);
        Order saved = orderService.save(order);

        cartService.clearCart(userId);
        return saved;
    }

    private void validatePrice(CartItem cartItem, Product current) {
        if (current.getPrice().compareTo(cartItem.price()) != 0) {
            throw new PriceChangedException(
                    cartItem.productId(),
                    cartItem.price(),
                    current.getPrice()
            );
        }
    }

    private OrderItem buildOrderItem(CartItem cartItem) {
        return new OrderItem(
                cartItem.productId(),
                cartItem.name(),
                cartItem.price(),
                cartItem.amount()
        );
    }
}
