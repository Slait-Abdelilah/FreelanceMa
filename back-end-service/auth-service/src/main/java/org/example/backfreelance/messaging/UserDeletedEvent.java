package org.example.backfreelance.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Événement publié quand un compte est supprimé
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeletedEvent {
    private Long   userId;
    private String email;
}
