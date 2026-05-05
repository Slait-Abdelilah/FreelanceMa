package org.example.backfreelance.repository;

import org.example.backfreelance.entity.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer,Long> {
    Optional<Freelancer> findByEmail(String email);
    List<Freelancer> findByIsAvailableTrue();
}
