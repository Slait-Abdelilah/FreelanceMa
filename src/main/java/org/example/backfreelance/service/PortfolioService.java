package org.example.backfreelance.service;

import lombok.AllArgsConstructor;
import org.example.backfreelance.dto.CreatePortfolioRequest;
import org.example.backfreelance.dto.PortfolioDTO;
import org.example.backfreelance.entity.Freelancer;
import org.example.backfreelance.entity.Portfolio;
import org.example.backfreelance.entity.User;
import org.example.backfreelance.repository.PortfolioRepository;
import org.example.backfreelance.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    // ===== RÉCUPÉRER MES PROJETS =====
    public List<PortfolioDTO> getMyPortfolio(String email) {

        Freelancer freelancer = getFreelancer(email);

        return portfolioRepository
                .findByFreelancerIdOrderByDisplayOrderAsc(freelancer.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ===== AJOUTER UN PROJET =====
    public PortfolioDTO addProject(String email, CreatePortfolioRequest request) {

        Freelancer freelancer = getFreelancer(email);

        // compter les projets existants pour l'ordre
        int order = portfolioRepository.countByFreelancerId(freelancer.getId());

        Portfolio portfolio = Portfolio.builder()
                .freelancerId(freelancer.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .technologies(request.getTechnologies())
                .imageUrl(request.getImageUrl())
                .projectUrl(request.getProjectUrl())
                .githubUrl(request.getGithubUrl())
                .displayOrder(order)
                .build();

        return toDTO(portfolioRepository.save(portfolio));
    }

    // ===== MODIFIER UN PROJET =====
    public PortfolioDTO updateProject(String email, Long projectId,
                                      CreatePortfolioRequest request) {

        Freelancer freelancer = getFreelancer(email);

        Portfolio portfolio = portfolioRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        // vérifier que le projet appartient au freelancer
        if (!portfolio.getFreelancerId().equals(freelancer.getId())) {
            throw new RuntimeException("Accès refusé");
        }

        if (request.getTitle() != null) portfolio.setTitle(request.getTitle());
        if (request.getDescription() != null) portfolio.setDescription(request.getDescription());
        if (request.getTechnologies() != null) portfolio.setTechnologies(request.getTechnologies());
        if (request.getImageUrl() != null) portfolio.setImageUrl(request.getImageUrl());
        if (request.getProjectUrl() != null) portfolio.setProjectUrl(request.getProjectUrl());
        if (request.getGithubUrl() != null) portfolio.setGithubUrl(request.getGithubUrl());

        return toDTO(portfolioRepository.save(portfolio));
    }

    // ===== SUPPRIMER UN PROJET =====
    public void deleteProject(String email, Long projectId) {

        Freelancer freelancer = getFreelancer(email);

        Portfolio portfolio = portfolioRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        if (!portfolio.getFreelancerId().equals(freelancer.getId())) {
            throw new RuntimeException("Accès refusé");
        }

        portfolioRepository.delete(portfolio);
    }

    // ===== RÉCUPÉRER PORTFOLIO PUBLIC =====
    public List<PortfolioDTO> getPublicPortfolio(Long freelancerId) {
        return portfolioRepository
                .findByFreelancerIdOrderByDisplayOrderAsc(freelancerId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ===== HELPER : récupérer le freelancer =====
    private Freelancer getFreelancer(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!(user instanceof Freelancer freelancer)) {
            throw new RuntimeException("Action réservée aux freelancers");
        }

        return freelancer;
    }

    // ===== HELPER : convertir en DTO =====
    private PortfolioDTO toDTO(Portfolio p) {
        return PortfolioDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .technologies(p.getTechnologies())
                .imageUrl(p.getImageUrl())
                .projectUrl(p.getProjectUrl())
                .githubUrl(p.getGithubUrl())
                .displayOrder(p.getDisplayOrder())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
