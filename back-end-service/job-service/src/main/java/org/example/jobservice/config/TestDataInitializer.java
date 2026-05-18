package org.example.jobservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobservice.entity.Application;
import org.example.jobservice.entity.Offer;
import org.example.jobservice.enums.ApplicationStatus;
import org.example.jobservice.enums.BudgetType;
import org.example.jobservice.enums.OfferCategory;
import org.example.jobservice.enums.OfferStatus;
import org.example.jobservice.repository.ApplicationRepository;
import org.example.jobservice.repository.OfferRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInitializer implements ApplicationRunner {

    private final OfferRepository offerRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (applicationRepository.count() > 0) {
            log.info("TestDataInitializer — données déjà présentes, skip");
            return;
        }

        Offer offer = offerRepository.save(Offer.builder()
                .clientId(1L)
                .title("Mission Test Messagerie")
                .description("Offre créée automatiquement pour tester la messagerie Firebase")
                .category(OfferCategory.WEB_DEVELOPMENT)
                .budgetType(BudgetType.FIXED)
                .budgetMin(BigDecimal.valueOf(500))
                .budgetMax(BigDecimal.valueOf(500))
                .status(OfferStatus.IN_PROGRESS)
                .build());

        Application app = applicationRepository.save(Application.builder()
                .offerId(offer.getId())
                .freelancerId(2L)
                .coverLetter("Candidature test pour messagerie")
                .proposedBudget(BigDecimal.valueOf(500))
                .proposedDays(10)
                .status(ApplicationStatus.ACCEPTED)
                .build());

        log.info("TestDataInitializer — offerId={} applicationId={} status={}",
                offer.getId(), app.getId(), app.getStatus());
    }
}
