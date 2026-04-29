package com.pedroheing.shoppingcart.user;

import com.pedroheing.shoppingcart.common.exception.NotFoundException;
import com.pedroheing.shoppingcart.user.dto.CreateUserInput;
import com.pedroheing.shoppingcart.user.dto.UpdateUserInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

    private User user() {
        return User.builder()
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .token("token-abc")
                .build();
    }

    @Test
    void create_validInput_generatesTokenAndSavesUser() {
        var input = new CreateUserInput("Alice Johnson", "alice.johnson@example.com");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = userService.create(input);

        assertThat(result.getName()).isEqualTo(input.name());
        assertThat(result.getEmail()).isEqualTo(input.email());
        assertThat(result.getToken()).isNotBlank();
        verify(userRepository).save(any());
    }

    @Test
    void findById_userExists_returnsUser() {
        var userId = "id-1";
        var u = user();
        when(userRepository.findById(userId)).thenReturn(Optional.of(u));

        assertThat(userService.findById(userId)).isEqualTo(u);
    }

    @Test
    void findById_userNotFound_throwsNotFoundException() {
        var userId = "id-1";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_userExists_appliesChangesAndSaves() {
        var userId = "id-1";
        var u = user();
        var newName = "Bob Smith";
        var input = new UpdateUserInput(Optional.of(newName), Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(userRepository.save(u)).thenReturn(u);

        var result = userService.update(userId, input);

        assertThat(result.getName()).isEqualTo(newName);
    }

    @Test
    void update_userNotFound_throwsNotFoundException() {
        var userId = "id-1";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(userId,
                new UpdateUserInput(Optional.empty(), Optional.empty())))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_userExists_deletesById() {
        var userId = "id-1";
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void delete_userNotFound_throwsNotFoundException() {
        var userId = "id-1";
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void generateToken_returnsValidBase64UrlSafeString() {
        var token = userService.generateToken();

        assertThat(token).isNotBlank();
        assertThatCode(() -> Base64.getUrlDecoder().decode(token)).doesNotThrowAnyException();
        assertThat(Base64.getUrlDecoder().decode(token)).hasSize(32);
    }

    @Test
    void generateToken_calledTwice_returnsDifferentTokens() {
        assertThat(userService.generateToken()).isNotEqualTo(userService.generateToken());
    }
}
