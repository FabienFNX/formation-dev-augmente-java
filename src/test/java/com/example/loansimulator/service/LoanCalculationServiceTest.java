package com.example.loansimulator.service;

import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanCalculationServiceTest {

    @Mock
    private InterestRateService interestRateService;

    @InjectMocks
    private LoanCalculationService loanCalculationService;

    @Test
    void testCalculateLoanWithManualRate() {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(20);
        request.setAnnualInterestRate(2.5);
        request.setFirstName("John");
        request.setLastName("Doe");

        // When
        LoanResponse response = loanCalculationService.calculateLoan(request);

        // Then
        assertNotNull(response);
        assertEquals(200000.0, response.getLoanAmount());
        assertEquals(20, response.getDurationYears());
        assertEquals(2.5, response.getAnnualInterestRate());
        assertTrue(response.getMonthlyPayment() > 0);
        assertTrue(response.getTotalInterest() > 0);
        assertTrue(response.getTotalCost() > response.getLoanAmount());

        // Vérifier que le service InterestRateService n'a pas été appelé
        verifyNoInteractions(interestRateService);
    }

    @Test
    void testCalculateLoanWithAutomaticRateCalculation() {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(300000.0);
        request.setDurationYears(25);
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setAgeCategory("ADULT");
        request.setProfessionalCategory("EXECUTIVE");
        request.setMonthlyNetIncome(5000.0);
        // annualInterestRate est null, donc sera calculé automatiquement

        when(interestRateService.calculateInterestRate(anyString(), anyString(), anyDouble()))
            .thenReturn(1.8);

        // When
        LoanResponse response = loanCalculationService.calculateLoan(request);

        // Then
        assertNotNull(response);
        assertEquals(300000.0, response.getLoanAmount());
        assertEquals(25, response.getDurationYears());
        assertEquals(1.8, response.getAnnualInterestRate());
        assertTrue(response.getMonthlyPayment() > 0);
        assertTrue(response.getTotalInterest() > 0);
        assertTrue(response.getTotalCost() > response.getLoanAmount());

        // Vérifier que le service InterestRateService a été appelé
        verify(interestRateService).calculateInterestRate("ADULT", "EXECUTIVE", 5000.0);
    }

    @Test
    void testCalculateLoanWithZeroInterestRate() {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(100000.0);
        request.setDurationYears(10);
        request.setAnnualInterestRate(0.0);

        // When
        LoanResponse response = loanCalculationService.calculateLoan(request);

        // Then
        assertNotNull(response);
        assertEquals(100000.0, response.getLoanAmount());
        assertEquals(10, response.getDurationYears());
        assertEquals(0.0, response.getAnnualInterestRate());
        assertEquals(0.0, response.getTotalInterest());
        assertEquals(100000.0, response.getTotalCost());
        assertEquals(100000.0 / (10 * 12), response.getMonthlyPayment(), 0.01);
    }

    @Test
    void testCalculateLoanWithFallbackRate() {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(150000.0);
        request.setDurationYears(15);
        // Pas de taux manuel, pas de données pour le calcul automatique

        // When
        LoanResponse response = loanCalculationService.calculateLoan(request);

        // Then
        assertNotNull(response);
        assertEquals(150000.0, response.getLoanAmount());
        assertEquals(15, response.getDurationYears());
        assertEquals(1.5, response.getAnnualInterestRate()); // Taux par défaut
        assertTrue(response.getMonthlyPayment() > 0);
        assertTrue(response.getTotalInterest() > 0);
        assertTrue(response.getTotalCost() > response.getLoanAmount());

        verifyNoInteractions(interestRateService);
    }

    @Test
    void testCalculateLoanWithPartialAutomaticData() {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(250000.0);
        request.setDurationYears(20);
        request.setAgeCategory("YOUNG_ADULT");
        // Manque professionalCategory et monthlyNetIncome

        // When
        LoanResponse response = loanCalculationService.calculateLoan(request);

        // Then
        assertNotNull(response);
        assertEquals(1.5, response.getAnnualInterestRate()); // Fallback rate
        verifyNoInteractions(interestRateService);
    }

    @Test
    void testCalculateMonthlyPaymentFormula() {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(20);
        request.setAnnualInterestRate(3.0);

        // When
        LoanResponse response = loanCalculationService.calculateLoan(request);

        // Then
        double expectedMonthlyRate = 3.0 / 100.0 / 12.0;
        int totalMonths = 20 * 12;
        double expectedMonthlyPayment = 200000.0 * (expectedMonthlyRate * Math.pow(1 + expectedMonthlyRate, totalMonths)) /
                                       (Math.pow(1 + expectedMonthlyRate, totalMonths) - 1);

        assertEquals(expectedMonthlyPayment, response.getMonthlyPayment(), 0.01);
        assertEquals(expectedMonthlyPayment * totalMonths, response.getTotalCost(), 0.01);
        assertEquals(response.getTotalCost() - 200000.0, response.getTotalInterest(), 0.01);
    }

    @Test
    void testCalculateLoanWithInterestRateServiceException() {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(300000.0);
        request.setDurationYears(25);
        request.setAgeCategory("ADULT");
        request.setProfessionalCategory("EMPLOYEE");
        request.setMonthlyNetIncome(3000.0);

        // Simuler que le service InterestRateService retourne le taux par défaut lors d'une erreur
        when(interestRateService.calculateInterestRate(anyString(), anyString(), anyDouble()))
            .thenReturn(1.5);

        // When
        LoanResponse response = loanCalculationService.calculateLoan(request);

        // Then
        assertNotNull(response);
        // Le service devrait utiliser le taux retourné par InterestRateService (1.5%)
        assertEquals(1.5, response.getAnnualInterestRate());
    }
}