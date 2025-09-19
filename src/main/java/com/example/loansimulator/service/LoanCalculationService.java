package com.example.loansimulator.service;

import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(LoanCalculationService.class);

    @Autowired
    private InterestRateService interestRateService;

    public LoanResponse calculateLoan(LoanRequest request) {
        double principal = request.getAmount();

        // Déterminer le taux d'intérêt (calculé automatiquement ou manuel)
        double annualRate = determineInterestRate(request);

        double monthlyRate = annualRate / 100.0 / 12.0;
        int totalMonths = request.getDurationYears() * 12;

        double monthlyPayment = 0.0;
        if (monthlyRate > 0) {
            monthlyPayment = principal * (monthlyRate * Math.pow(1 + monthlyRate, totalMonths)) /
                            (Math.pow(1 + monthlyRate, totalMonths) - 1);
        } else {
            monthlyPayment = principal / totalMonths;
        }

        double totalAmountPaid = monthlyPayment * totalMonths;
        double totalInterest = totalAmountPaid - principal;

        return new LoanResponse(
            principal,
            totalInterest,
            totalAmountPaid,
            monthlyPayment,
            request.getDurationYears(),
            annualRate
        );
    }

    private double determineInterestRate(LoanRequest request) {
        // Si le taux est fourni manuellement (interface client), l'utiliser
        if (request.getAnnualInterestRate() != null && request.getAnnualInterestRate() >= 0) {
            logger.info("Using manual interest rate: {}%", request.getAnnualInterestRate());
            return request.getAnnualInterestRate();
        }

        // Sinon, calculer automatiquement via l'API Interest Rate (interface employé)
        if (request.getAgeCategory() != null &&
            request.getProfessionalCategory() != null &&
            request.getMonthlyNetIncome() != null) {

            double calculatedRate = interestRateService.calculateInterestRate(
                request.getAgeCategory(),
                request.getProfessionalCategory(),
                request.getMonthlyNetIncome()
            );

            logger.info("Calculated automatic interest rate: {}% for client: {} {} (Age: {}, Prof: {}, Income: {}€)",
                calculatedRate, request.getFirstName(), request.getLastName(),
                request.getAgeCategory(), request.getProfessionalCategory(), request.getMonthlyNetIncome());

            return calculatedRate;
        }

        // Fallback - taux par défaut
        logger.warn("No rate calculation possible, using default rate: 1.5%");
        return 1.5;
    }
}