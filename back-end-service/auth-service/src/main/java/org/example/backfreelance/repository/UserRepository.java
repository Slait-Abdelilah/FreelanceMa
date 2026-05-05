package org.example.backfreelance.repository;

import org.example.backfreelance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    // chercher par token de réinitialisation
    Optional<User> findByResetPasswordToken(String token);

}
