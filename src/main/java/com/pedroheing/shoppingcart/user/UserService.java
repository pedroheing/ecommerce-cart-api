package com.pedroheing.shoppingcart.user;

import com.pedroheing.shoppingcart.common.exception.NotFoundException;
import com.pedroheing.shoppingcart.user.dto.CreateUserInput;
import com.pedroheing.shoppingcart.user.dto.UpdateUserInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(CreateUserInput input) {
        User user = User.builder()
                .name(input.name())
                .email(input.email())
                .token(this.generateToken())
                .build();
        return userRepository.save(user);
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public Optional<User> findByToken(String token) {
         return userRepository.findByToken(token);
    }

    @Transactional
    public User update(String id, UpdateUserInput input) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        user.setName(input.name());
        user.setEmail(input.email());
        return userRepository.save(user);
    }

    @Transactional
    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    public String generateToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
