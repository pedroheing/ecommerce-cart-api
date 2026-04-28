package com.pedroheing.shoppingcart.order;

import com.pedroheing.shoppingcart.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }

    public List<Order> findByUser(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}