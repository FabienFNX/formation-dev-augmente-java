package com.example.interestrateapi.service;

import com.example.interestrateapi.config.InterestRateConfig;
import com.example.interestrateapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class InterestRateCalculationService {

    @Autowired
    private InterestRateConfig config;

    public InterestRateResponse calculateInterestRate(InterestRateRequest request) {
        double baseRate = config.getBaseRate();
        double ageModifier = request.getAgeCategory().getRateModifier();
        double professionalModifier = request.getProfessionalCategory().getRateModifier();
        double incomeModifier = calculateIncomeModifier(request.getMonthlyNetIncome());

        double finalRate = baseRate + ageModifier + professionalModifier + incomeModifier;

        return new InterestRateResponse(
                Math.round(finalRate * 100.0) / 100.0, // Arrondi à 2 décimales
                baseRate,
                ageModifier,
                professionalModifier,
                incomeModifier,
                request.getAgeCategory(),
                request.getProfessionalCategory(),
                request.getMonthlyNetIncome()
        );
    }

    public List<CategoryInfo> getAgeCategories() {
        return Arrays.stream(AgeCategory.values())
                .map(category -> new CategoryInfo(
                        category.name(),
                        category.getDescription(),
                        category.getAgeRange(),
                        category.getRateModifier()
                ))
                .toList();
    }

    public List<CategoryInfo> getProfessionalCategories() {
        return Arrays.stream(ProfessionalCategory.values())
                .map(category -> new CategoryInfo(
                        category.name(),
                        category.getName(),
                        category.getDescription(),
                        category.getRateModifier()
                ))
                .toList();
    }

    public InterestRateConfig getCurrentConfig() {
        return config;
    }

    private double calculateIncomeModifier(double monthlyIncome) {
        InterestRateConfig.IncomeThresholds thresholds = config.getIncomeThresholds();

        if (monthlyIncome < thresholds.getLow()) {
            return thresholds.getLowModifier();
        } else if (monthlyIncome < thresholds.getMedium()) {
            return thresholds.getMediumModifier();
        } else if (monthlyIncome < thresholds.getHigh()) {
            return thresholds.getHighModifier();
        } else {
            return thresholds.getVeryHighModifier();
        }
    }
}