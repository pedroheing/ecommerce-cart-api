package com.pedroheing.shoppingcart.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    private Product valid() {
        return Product.builder().name("Wireless Headphones").stock(10).price(new BigDecimal("99.99")).build();
    }

    @Test
    void constructor_validArgs_createsProduct() {
        var name = "Wireless Headphones";
        var stock = 10;
        var price = new BigDecimal("99.99");

        var p = Product.builder().name(name).stock(stock).price(price).build();

        assertThat(p.getName()).isEqualTo(name);
        assertThat(p.getStock()).isEqualTo(stock);
        assertThat(p.getPrice()).isEqualByComparingTo(price);
    }

    @Test
    void constructor_nullName_throwsIllegalArgument() {
        assertThatThrownBy(() -> Product.builder().name(null).stock(5).price(BigDecimal.ONE).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_blankName_throwsIllegalArgument() {
        assertThatThrownBy(() -> Product.builder().name("  ").stock(5).price(BigDecimal.ONE).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_negativeStock_throwsIllegalArgument() {
        assertThatThrownBy(() -> Product.builder().name("P").stock(-1).price(BigDecimal.ONE).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_nullPrice_throwsIllegalArgument() {
        assertThatThrownBy(() -> Product.builder().name("P").stock(1).price(null).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_negativePrice_throwsIllegalArgument() {
        assertThatThrownBy(() -> Product.builder().name("P").stock(1).price(new BigDecimal("-0.01")).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeName_validName_updatesName() {
        var p = valid();
        var newName = "Noise Cancelling Headphones";
        p.changeName(newName);
        assertThat(p.getName()).isEqualTo(newName);
    }

    @Test
    void changeName_blankName_throwsIllegalArgument() {
        assertThatThrownBy(() -> valid().changeName(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePrice_validPrice_updatesPrice() {
        var p = valid();
        var newPrice = new BigDecimal("149.99");
        p.changePrice(newPrice);
        assertThat(p.getPrice()).isEqualByComparingTo(newPrice);
    }

    @Test
    void changePrice_negativePrice_throwsIllegalArgument() {
        assertThatThrownBy(() -> valid().changePrice(new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeStock_validAmount_updatesStock() {
        var p = valid();
        var newStock = 25;
        p.changeStock(newStock);
        assertThat(p.getStock()).isEqualTo(newStock);
    }

    @Test
    void changeStock_negativeAmount_throwsIllegalArgument() {
        assertThatThrownBy(() -> valid().changeStock(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeStock_zero_setsStockToZero() {
        var p = valid();
        p.changeStock(0);
        assertThat(p.getStock()).isZero();
    }
}
