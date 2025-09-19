package com.example.loansimulator.controller;

import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import com.example.loansimulator.service.LoanCalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanCalculationService loanCalculationService;

    @Test
    void testCalculateLoanWithValidClientRequest() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(20);
        request.setAnnualInterestRate(2.5);

        LoanResponse mockResponse = new LoanResponse(
            200000.0,
            54353.39,
            254353.39,
            1059.81,
            20,
            2.5
        );

        when(loanCalculationService.calculateLoan(any(LoanRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.loanAmount").value(200000.0))
                .andExpect(jsonPath("$.totalInterest").value(54353.39))
                .andExpect(jsonPath("$.totalCost").value(254353.39))
                .andExpect(jsonPath("$.monthlyPayment").value(1059.81))
                .andExpect(jsonPath("$.durationYears").value(20))
                .andExpect(jsonPath("$.annualInterestRate").value(2.5));

        verify(loanCalculationService).calculateLoan(any(LoanRequest.class));
    }

    @Test
    void testCalculateLoanWithValidEmployeeRequest() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setFirstName("Jean");
        request.setLastName("Dupont");
        request.setAgeCategory("ADULT");
        request.setProfessionalCategory("EXECUTIVE");
        request.setMonthlyNetIncome(4500.0);
        request.setAmount(300000.0);
        request.setDurationYears(25);
        // annualInterestRate est null (calculé automatiquement)

        LoanResponse mockResponse = new LoanResponse(
            300000.0,
            65432.10,
            365432.10,
            1218.11,
            25,
            1.2
        );

        when(loanCalculationService.calculateLoan(any(LoanRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.loanAmount").value(300000.0))
                .andExpect(jsonPath("$.annualInterestRate").value(1.2));

        verify(loanCalculationService).calculateLoan(any(LoanRequest.class));
    }

    @Test
    void testCalculateLoanWithInvalidAmount() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(0.0); // Invalid amount
        request.setDurationYears(20);
        request.setAnnualInterestRate(2.5);

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateLoanWithInvalidDuration() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(0); // Invalid duration
        request.setAnnualInterestRate(2.5);

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateLoanWithNegativeInterestRate() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(20);
        request.setAnnualInterestRate(-1.0); // Invalid negative rate

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateLoanWithNullInterestRate() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(20);
        // annualInterestRate is null - should be valid for employee interface

        LoanResponse mockResponse = new LoanResponse(
            200000.0,
            45000.0,
            245000.0,
            1020.83,
            20,
            1.5
        );

        when(loanCalculationService.calculateLoan(any(LoanRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testCalculateLoanWithServiceException() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(20);
        request.setAnnualInterestRate(2.5);

        when(loanCalculationService.calculateLoan(any(LoanRequest.class)))
            .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCalculateLoanWithMalformedJson() throws Exception {
        // Given
        String malformedJson = "{ \"amount\": \"invalid\" }";

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateLoanWithEmptyBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateLoanCorsHeaders() throws Exception {
        // Given
        LoanRequest request = new LoanRequest();
        request.setAmount(200000.0);
        request.setDurationYears(20);
        request.setAnnualInterestRate(2.5);

        LoanResponse mockResponse = new LoanResponse(
            200000.0, 50000.0, 250000.0, 1041.67, 20, 2.5
        );

        when(loanCalculationService.calculateLoan(any(LoanRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/calculate-loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }
}