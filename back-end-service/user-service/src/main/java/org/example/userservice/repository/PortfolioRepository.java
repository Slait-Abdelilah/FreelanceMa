package org.example.userservice.repository;

import org.example.userservice.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByFreelancerIdOrderByDisplayOrderAsc(Long freelancerId);
    int countByFreelancerId(Long freelancerId);
}
