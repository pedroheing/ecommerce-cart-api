package com.pedroheing.shoppingcart.cart;

import com.pedroheing.shoppingcart.cart.dto.PutItemInput;
import com.pedroheing.shoppingcart.product.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(
        CartRepository cartRepository,
        @Qualifier("cachedProductService") ProductService productService
    ) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public CartItem putItem(PutItemInput input) {
        var product = this.productService.findById(input.productId());
        var cartItem = new CartItem(input.productId(), product.getName(), product.getPrice(), input.amount());
        this.cartRepository.putItem(input.userId(), cartItem);
        return cartItem;
    }

    public void removeItem(String userId, String productId) {
        this.cartRepository.removeItem(userId, productId);
    }

    public void clearCart(String userId) {
        this.cartRepository.clearCart(userId);
    }

    public List<CartItem> listItems(String userId) {
        return this.cartRepository.listItems(userId);
    }
}
