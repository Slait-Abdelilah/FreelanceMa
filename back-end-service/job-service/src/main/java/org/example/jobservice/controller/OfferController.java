package org.example.jobservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.CreateOfferRequest;
import org.example.jobservice.dto.OfferDTO;
import org.example.jobservice.service.OfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<Page<OfferDTO>> getAllOffers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (page < 0) page = 0;
        if (size < 1) size = 1;
        if (size > 100) size = 100;

        PageRequest pageable = PageRequest.of(page, size);

        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(offerService.searchOffers(search, pageable));
        }
        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(offerService.filterByCategory(category, pageable));
        }
        return ResponseEntity.ok(offerService.getAllOpenOffers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    // CLIENT seulement — contrôlé dans SecurityConfig
    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(
            Authentication auth,
            @Valid @RequestBody CreateOfferRequest request) {
        Long clientId = (Long) auth.getDetails();
        return ResponseEntity.status(201).body(offerService.createOffer(clientId, request));
    }
}