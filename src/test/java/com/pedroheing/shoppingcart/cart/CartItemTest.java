package com.pedroheing.shoppingcart.cart;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class CartItemTest {

    private CartItem valid() {
        return new CartItem("prod-1", "Wireless Headphones", new BigDecimal("99.99"), 2);
    }

    @Test
    void constructor_validArgs_createsCartItem() {
        var productId = "prod-1";
        var name = "Wireless Headphones";
        var price = new BigDecimal("99.99");
        var amount = 2;

        var item = new CartItem(productId, name, price, amount);

        assertThat(item.productId()).isEqualTo(productId);
        assertThat(item.name()).isEqualTo(name);
        assertThat(item.price()).isEqualByComparingTo(price);
        assertThat(item.amount()).isEqualTo(amount);
    }

    @Test
    void constructor_blankProductId_throwsIllegalArgument() {
        assertThatThrownBy(() -> new CartItem("", "P", BigDecimal.ONE, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_nullProductId_throwsIllegalArgument() {
        assertThatThrownBy(() -> new CartItem(null, "P", BigDecimal.ONE, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_blankName_throwsIllegalArgument() {
        assertThatThrownBy(() -> new CartItem("prod-1", "", BigDecimal.ONE, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_negativePrice_throwsIllegalArgument() {
        assertThatThrownBy(() -> new CartItem("prod-1", "P", new BigDecimal("-0.01"), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_nullPrice_throwsIllegalArgument() {
        assertThatThrownBy(() -> new CartItem("prod-1", "P", null, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_zeroAmount_throwsIllegalArgument() {
        assertThatThrownBy(() -> new CartItem("prod-1", "P", BigDecimal.ONE, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_negativeAmount_throwsIllegalArgument() {
        assertThatThrownBy(() -> new CartItem("prod-1", "P", BigDecimal.ONE, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withAmount_validAmount_returnsNewItemWithUpdatedAmount() {
        var original = valid();
        var newAmount = 5;

        var updated = original.withAmount(newAmount);

        assertThat(updated.amount()).isEqualTo(newAmount);
        assertThat(updated.productId()).isEqualTo(original.productId());
        assertThat(updated.name()).isEqualTo(original.name());
    }

    @Test
    void withAmount_zeroAmount_throwsIllegalArgument() {
        assertThatThrownBy(() -> valid().withAmount(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
