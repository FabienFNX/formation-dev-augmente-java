package com.example.loansimulator.service;

import com.example.loansimulator.dto.CategoryInfo;
import com.example.loansimulator.dto.InterestRateRequest;
import com.example.loansimulator.dto.InterestRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
public class InterestRateService {

    private static final Logger logger = LoggerFactory.getLogger(InterestRateService.class);

    private final WebClient webClient;

    @Value("${interest-rate-api.base-url}")
    private String baseUrl;

    public InterestRateService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    public double calculateInterestRate(String ageCategory, String professionalCategory, Double monthlyNetIncome) {
        try {
            InterestRateRequest request = new InterestRateRequest(ageCategory, professionalCategory, monthlyNetIncome);

            InterestRateResponse response = webClient
                    .post()
                    .uri(baseUrl + "/api/interest-rate/calculate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(InterestRateResponse.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            if (response != null) {
                logger.info("Calculated interest rate: {}% for profile: {}, {}, {}€",
                    response.getAnnualInterestRate(), ageCategory, professionalCategory, monthlyNetIncome);
                return response.getAnnualInterestRate();
            } else {
                logger.warn("No response from interest rate API, using default rate");
                return getDefaultInterestRate();
            }

        } catch (Exception e) {
            logger.error("Error calling interest rate API: {}", e.getMessage());
            return getDefaultInterestRate();
        }
    }

    public List<CategoryInfo> getAgeCategories() {
        try {
            return webClient
                    .get()
                    .uri(baseUrl + "/api/interest-rate/categories/age")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CategoryInfo>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception e) {
            logger.error("Error fetching age categories: {}", e.getMessage());
            return List.of();
        }
    }

    public List<CategoryInfo> getProfessionalCategories() {
        try {
            return webClient
                    .get()
                    .uri(baseUrl + "/api/interest-rate/categories/professional")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CategoryInfo>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception e) {
            logger.error("Error fetching professional categories: {}", e.getMessage());
            return List.of();
        }
    }

    private double getDefaultInterestRate() {
        return 1.5; // Taux par défaut en cas d'erreur
    }
}