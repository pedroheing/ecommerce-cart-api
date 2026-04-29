package com.pedroheing.shoppingcart.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class OrderItemTest {

    @Test
    void constructor_validArgs_calculatesSubtotal() {
        var unitPrice = new BigDecimal("50.00");
        var amount = 3;

        var item = new OrderItem("prod-1", "Wireless Headphones", unitPrice, amount);

        assertThat(item.getUnitPrice()).isEqualByComparingTo(unitPrice);
        assertThat(item.getAmount()).isEqualTo(amount);
        assertThat(item.getSubtotal()).isEqualByComparingTo(unitPrice.multiply(BigDecimal.valueOf(amount)));
    }

    @Test
    void constructor_unitPriceOne_subtotalEqualsAmount() {
        var amount = 7;

        var item = new OrderItem("prod-1", "P", BigDecimal.ONE, amount);

        assertThat(item.getSubtotal()).isEqualByComparingTo(BigDecimal.valueOf(amount));
    }

    @Test
    void constructor_blankProductId_throwsIllegalArgument() {
        assertThatThrownBy(() -> new OrderItem("", "P", BigDecimal.ONE, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_nullProductId_throwsIllegalArgument() {
        assertThatThrownBy(() -> new OrderItem(null, "P", BigDecimal.ONE, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_blankProductName_throwsIllegalArgument() {
        assertThatThrownBy(() -> new OrderItem("prod-1", "", BigDecimal.ONE, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_negativeUnitPrice_throwsIllegalArgument() {
        assertThatThrownBy(() -> new OrderItem("prod-1", "P", new BigDecimal("-0.01"), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_zeroAmount_throwsIllegalArgument() {
        assertThatThrownBy(() -> new OrderItem("prod-1", "P", BigDecimal.ONE, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_negativeAmount_throwsIllegalArgument() {
        assertThatThrownBy(() -> new OrderItem("prod-1", "P", BigDecimal.ONE, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
