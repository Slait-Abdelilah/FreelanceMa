package org.example.backfreelance.repository;

import org.example.backfreelance.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByFreelancerIdOrderByDisplayOrderAsc(Long freelancerId);
    int countByFreelancerId(Long freelancerId);
}
