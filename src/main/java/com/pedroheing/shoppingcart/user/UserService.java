package com.pedroheing.shoppingcart.user;

import com.pedroheing.shoppingcart.common.exception.NotFoundException;
import com.pedroheing.shoppingcart.user.dto.CreateUserInput;
import com.pedroheing.shoppingcart.user.dto.UpdateUserInput;
import com.pedroheing.shoppingcart.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDTO create(CreateUserInput input) {
        User user = User.builder()
                .name(input.name())
                .email(input.email())
                .build();
        return toDTO(userRepository.save(user));
    }

    public UserDTO findById(String id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Transactional
    public UserDTO update(String id, UpdateUserInput input) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        user.setName(input.name());
        user.setEmail(input.email());
        return toDTO(userRepository.save(user));
    }

    @Transactional
    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
