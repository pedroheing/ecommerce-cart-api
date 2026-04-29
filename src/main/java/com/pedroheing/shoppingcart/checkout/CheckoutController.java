package com.pedroheing.shoppingcart.checkout;

import com.pedroheing.shoppingcart.auth.CurrentUser;
import com.pedroheing.shoppingcart.order.Order;
import com.pedroheing.shoppingcart.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<OrderResponse> checkout(@CurrentUser User user) {
        var order = checkoutService.checkout(user.getId());
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
