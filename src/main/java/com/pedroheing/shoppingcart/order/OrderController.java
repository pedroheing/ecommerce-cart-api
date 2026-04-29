package com.pedroheing.shoppingcart.order;

import com.pedroheing.shoppingcart.auth.CurrentUser;
import com.pedroheing.shoppingcart.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders")
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(OrderResponse.from(orderService.findById(id)));
    }

    @SecurityRequirement(name = "bearer-token")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> listByUser(@CurrentUser User user) {
        var orders = orderService.findByUser(user.getId())
                .stream()
                .map(OrderResponse::from)
                .toList();
        return ResponseEntity.ok(orders);
    }
}
