package com.example.loansimulator.controller;

import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import com.example.loansimulator.service.LoanCalculationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    private LoanCalculationService loanCalculationService;

    @PostMapping("/calculate-loan")
    public ResponseEntity<LoanResponse> calculateLoan(@RequestBody LoanRequest request) {
        try {
            logger.info("Received loan request: amount={}, duration={}, rate={}, firstName={}, ageCategory={}",
                request.getAmount(), request.getDurationYears(), request.getAnnualInterestRate(),
                request.getFirstName(), request.getAgeCategory());

            // Validation des champs obligatoires
            if (request.getAmount() <= 0 || request.getDurationYears() <= 0) {
                logger.warn("Invalid request: amount or duration is invalid");
                return ResponseEntity.badRequest().build();
            }

            // Pour l'interface client, le taux doit être fourni et >= 0
            // Pour l'interface employé, le taux peut être null (calculé automatiquement)
            if (request.getAnnualInterestRate() != null && request.getAnnualInterestRate() < 0) {
                logger.warn("Invalid request: interest rate is negative");
                return ResponseEntity.badRequest().build();
            }

            LoanResponse response = loanCalculationService.calculateLoan(request);
            logger.info("Loan calculation successful: monthly payment={}", response.getMonthlyPayment());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error calculating loan: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}