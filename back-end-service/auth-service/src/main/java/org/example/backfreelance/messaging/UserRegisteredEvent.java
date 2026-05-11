package org.example.backfreelance.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Événement publié quand un utilisateur s'inscrit
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {
    private Long   userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;     // "CLIENT" ou "FREELANCER"
}
