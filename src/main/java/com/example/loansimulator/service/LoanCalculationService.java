package com.example.loansimulator.service;

import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import org.springframework.stereotype.Service;

@Service
public class LoanCalculationService {

    public LoanResponse calculateLoan(LoanRequest request) {
        double principal = request.getAmount();
        double annualRate = request.getAnnualInterestRate() / 100.0;
        double monthlyRate = annualRate / 12.0;
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
            request.getAnnualInterestRate()
        );
    }
}