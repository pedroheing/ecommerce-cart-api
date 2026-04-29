package com.pedroheing.shoppingcart.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    private OrderItem item(BigDecimal unitPrice, int amount) {
        return new OrderItem("prod-1", "Wireless Headphones", unitPrice, amount);
    }

    @Test
    void constructor_validArgs_setsPendingStatusAndSumsTotal() {
        var userId = "user-1";
        var item1 = item(new BigDecimal("100.00"), 2);
        var item2 = item(new BigDecimal("50.00"), 1);
        var expectedTotal = item1.getSubtotal().add(item2.getSubtotal());

        var order = new Order(userId, List.of(item1, item2));

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTotal()).isEqualByComparingTo(expectedTotal);
        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getUserId()).isEqualTo(userId);
    }

    @Test
    void constructor_blankUserId_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Order("", List.of(item(BigDecimal.ONE, 1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_nullUserId_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Order(null, List.of(item(BigDecimal.ONE, 1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_emptyItems_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Order("user-1", List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_nullItems_throwsIllegalArgument() {
        assertThatThrownBy(() -> new Order("user-1", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void confirm_pendingOrder_statusBecomesConfirmed() {
        var order = new Order("user-1", List.of(item(BigDecimal.ONE, 1)));
        order.confirm();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void confirm_alreadyConfirmed_throwsIllegalState() {
        var order = new Order("user-1", List.of(item(BigDecimal.ONE, 1)));
        order.confirm();
        assertThatThrownBy(order::confirm).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void confirm_failedOrder_throwsIllegalState() {
        var order = new Order("user-1", List.of(item(BigDecimal.ONE, 1)));
        order.fail("error");
        assertThatThrownBy(order::confirm).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void fail_pendingOrder_statusBecomesFailed() {
        var order = new Order("user-1", List.of(item(BigDecimal.ONE, 1)));
        order.fail("payment declined");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.FAILED);
    }

    @Test
    void fail_alreadyFailed_throwsIllegalState() {
        var order = new Order("user-1", List.of(item(BigDecimal.ONE, 1)));
        order.fail("reason");
        assertThatThrownBy(() -> order.fail("reason")).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void fail_confirmedOrder_throwsIllegalState() {
        var order = new Order("user-1", List.of(item(BigDecimal.ONE, 1)));
        order.confirm();
        assertThatThrownBy(() -> order.fail("reason")).isInstanceOf(IllegalStateException.class);
    }
}
