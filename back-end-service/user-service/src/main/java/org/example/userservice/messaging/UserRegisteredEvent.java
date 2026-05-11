package org.example.userservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Structure identique à celle d'auth-service (même JSON)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {
    private Long   userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
