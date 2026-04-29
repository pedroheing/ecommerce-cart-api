package com.pedroheing.shoppingcart.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Schema(example = "Alice Johnson")
    @Column(nullable = false)
    private String name;

    @Schema(example = "alice.johnson@example.com")
    @Column(nullable = false)
    private String email;

    @Schema(example = "c9bf9e57-1685-4c89-bafb-ff5af830be8a")
    @Column(nullable = false, unique = true)
    private String token;

    public void changeName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public void changeEmail(String email) {
        if (email == null || name.isEmpty()) {
            throw new IllegalArgumentException("E-mail cannot be empty");
        }
        this.email = email;
    }
}