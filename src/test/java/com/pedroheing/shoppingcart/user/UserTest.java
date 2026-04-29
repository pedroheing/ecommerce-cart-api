package com.pedroheing.shoppingcart.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    private User user() {
        return User.builder()
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .token("token-abc")
                .build();
    }

    @Test
    void changeName_validName_updatesName() {
        var u = user();
        var newName = "Bob Smith";
        u.changeName(newName);
        assertThat(u.getName()).isEqualTo(newName);
    }

    @Test
    void changeName_nullName_throwsIllegalArgument() {
        assertThatThrownBy(() -> user().changeName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeName_emptyName_throwsIllegalArgument() {
        assertThatThrownBy(() -> user().changeName(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmail_validEmail_updatesEmail() {
        var u = user();
        var newEmail = "bob.smith@example.com";
        u.changeEmail(newEmail);
        assertThat(u.getEmail()).isEqualTo(newEmail);
    }
}
