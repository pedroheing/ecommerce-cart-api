package com.pedroheing.shoppingcart.user;

import com.pedroheing.shoppingcart.user.dto.CreateUserInput;
import com.pedroheing.shoppingcart.user.dto.CreateUserRequest;
import com.pedroheing.shoppingcart.user.dto.UpdateUserInput;
import com.pedroheing.shoppingcart.user.dto.UpdateUserRequest;
import com.pedroheing.shoppingcart.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserRequest request) {
        var input = new CreateUserInput(request.name(), request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(input));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request) {
        var input = new UpdateUserInput(request.name(), request.email());
        return ResponseEntity.ok(userService.update(id, input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
