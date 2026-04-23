package com.pedroheing.shoppingcart.user;

import com.pedroheing.shoppingcart.user.dto.CreateUserRequest;
import com.pedroheing.shoppingcart.user.dto.UpdateUserRequest;
import com.pedroheing.shoppingcart.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
