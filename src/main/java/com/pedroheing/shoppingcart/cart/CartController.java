package com.pedroheing.shoppingcart.cart;

import com.pedroheing.shoppingcart.auth.CurrentUser;
import com.pedroheing.shoppingcart.cart.dto.PutItemInput;
import com.pedroheing.shoppingcart.cart.dto.AddToCartRequest;
import com.pedroheing.shoppingcart.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartItem> putItem(@CurrentUser User user, @Valid @RequestBody AddToCartRequest request) {
        var input = new PutItemInput(user.getId(), request.productId(), request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.putItem(input));
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> removeFromCart(@CurrentUser User user, @PathVariable String productId) {
        cartService.removeItem(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> listItems(@CurrentUser User user) {
        return ResponseEntity.ok(cartService.listItems(user.getId()));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@CurrentUser User user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.noContent().build();
    }
}
