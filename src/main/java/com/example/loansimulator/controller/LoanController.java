package com.example.loansimulator.controller;

import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import com.example.loansimulator.service.LoanCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanCalculationService loanCalculationService;

    @PostMapping("/calculate-loan")
    public ResponseEntity<LoanResponse> calculateLoan(@RequestBody LoanRequest request) {
        try {
            if (request.getAmount() <= 0 || request.getDurationYears() <= 0 || request.getAnnualInterestRate() < 0) {
                return ResponseEntity.badRequest().build();
            }

            LoanResponse response = loanCalculationService.calculateLoan(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}