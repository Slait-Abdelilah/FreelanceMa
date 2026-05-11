package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.CreatePortfolioRequest;
import org.example.userservice.dto.PortfolioDTO;
import org.example.userservice.entity.Portfolio;
import org.example.userservice.exception.AppException;
import org.example.userservice.repository.PortfolioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public List<PortfolioDTO> getMyPortfolio(Long freelancerId) {
        return portfolioRepository.findByFreelancerIdOrderByDisplayOrderAsc(freelancerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public PortfolioDTO addProject(Long freelancerId, CreatePortfolioRequest request) {
        int order = portfolioRepository.countByFreelancerId(freelancerId);
        Portfolio p = Portfolio.builder()
                .freelancerId(freelancerId).title(request.getTitle())
                .description(request.getDescription()).technologies(request.getTechnologies())
                .imageUrl(request.getImageUrl()).projectUrl(request.getProjectUrl())
                .githubUrl(request.getGithubUrl()).displayOrder(order).build();
        return toDTO(portfolioRepository.save(p));
    }

    @Transactional
    public PortfolioDTO updateProject(Long freelancerId, Long projectId, CreatePortfolioRequest request) {
        Portfolio p = portfolioRepository.findById(projectId)
                .orElseThrow(() -> new AppException("Projet introuvable", HttpStatus.NOT_FOUND));
        if (!p.getFreelancerId().equals(freelancerId))
            throw new AppException("Accès refusé", HttpStatus.FORBIDDEN);

        if (request.getTitle() != null)        p.setTitle(request.getTitle());
        if (request.getDescription() != null)  p.setDescription(request.getDescription());
        if (request.getTechnologies() != null) p.setTechnologies(request.getTechnologies());
        if (request.getImageUrl() != null)     p.setImageUrl(request.getImageUrl());
        if (request.getProjectUrl() != null)   p.setProjectUrl(request.getProjectUrl());
        if (request.getGithubUrl() != null)    p.setGithubUrl(request.getGithubUrl());
        return toDTO(portfolioRepository.save(p));
    }

    @Transactional
    public void deleteProject(Long freelancerId, Long projectId) {
        Portfolio p = portfolioRepository.findById(projectId)
                .orElseThrow(() -> new AppException("Projet introuvable", HttpStatus.NOT_FOUND));
        if (!p.getFreelancerId().equals(freelancerId))
            throw new AppException("Accès refusé", HttpStatus.FORBIDDEN);
        portfolioRepository.delete(p);
    }

    public List<PortfolioDTO> getPublicPortfolio(Long freelancerId) {
        return portfolioRepository.findByFreelancerIdOrderByDisplayOrderAsc(freelancerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PortfolioDTO toDTO(Portfolio p) {
        return PortfolioDTO.builder().id(p.getId()).title(p.getTitle())
                .description(p.getDescription()).technologies(p.getTechnologies())
                .imageUrl(p.getImageUrl()).projectUrl(p.getProjectUrl())
                .githubUrl(p.getGithubUrl()).displayOrder(p.getDisplayOrder())
                .createdAt(p.getCreatedAt()).build();
    }
}
