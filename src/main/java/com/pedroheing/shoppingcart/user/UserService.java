package com.pedroheing.shoppingcart.user;

import com.pedroheing.shoppingcart.common.NotFoundException;
import com.pedroheing.shoppingcart.user.dto.CreateUserRequest;
import com.pedroheing.shoppingcart.user.dto.UpdateUserRequest;
import com.pedroheing.shoppingcart.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDTO create(CreateUserRequest request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .build();
        return toDTO(userRepository.save(user));
    }

    public UserDTO findById(String id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Transactional
    public UserDTO update(String id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        user.setName(request.name());
        user.setEmail(request.email());
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
