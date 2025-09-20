package com.example.loansimulator.controller;

import com.example.loansimulator.dto.LoanSimulationSummary;
import com.example.loansimulator.dto.SaveSimulationRequest;
import com.example.loansimulator.entity.LoanSimulation;
import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import com.example.loansimulator.service.LoanSimulationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanSimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanSimulationService loanSimulationService;

    private SaveSimulationRequest validSaveRequest;
    private LoanSimulation savedSimulation;
    private LoanSimulationSummary simulationSummary;

    @BeforeEach
    void setUp() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setFirstName("Jean");
        loanRequest.setLastName("Dupont");
        loanRequest.setAgeCategory("ADULT");
        loanRequest.setProfessionalCategory("EXECUTIVE");
        loanRequest.setMonthlyNetIncome(4500.0);
        loanRequest.setAmount(200000.0);
        loanRequest.setDurationYears(20);

        LoanResponse loanResponse = new LoanResponse(
            200000.0, 54353.39, 254353.39, 1059.81, 20, 2.5
        );

        validSaveRequest = new SaveSimulationRequest(loanRequest, loanResponse);

        savedSimulation = new LoanSimulation();
        savedSimulation.setId(1L);
        savedSimulation.setFirstName("Jean");
        savedSimulation.setLastName("Dupont");
        savedSimulation.setAgeCategory("ADULT");
        savedSimulation.setProfessionalCategory("EXECUTIVE");
        savedSimulation.setMonthlyNetIncome(4500.0);
        savedSimulation.setLoanAmount(200000.0);
        savedSimulation.setDurationYears(20);
        savedSimulation.setAnnualInterestRate(2.5);
        savedSimulation.setTotalInterest(54353.39);
        savedSimulation.setTotalCost(254353.39);
        savedSimulation.setMonthlyPayment(1059.81);
        savedSimulation.setCreatedAt(LocalDateTime.now());

        simulationSummary = new LoanSimulationSummary(savedSimulation);
    }

    @Test
    void testSaveSimulationSuccess() throws Exception {
        // Given
        when(loanSimulationService.canSaveSimulation(any(LoanRequest.class))).thenReturn(true);
        when(loanSimulationService.saveSimulation(any(LoanRequest.class), any(LoanResponse.class)))
            .thenReturn(savedSimulation);

        // When & Then
        mockMvc.perform(post("/api/save-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSaveRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientName").value("Jean Dupont"))
                .andExpect(jsonPath("$.loanAmount").value(200000.0))
                .andExpect(jsonPath("$.annualInterestRate").value(2.5));

        verify(loanSimulationService).canSaveSimulation(any(LoanRequest.class));
        verify(loanSimulationService).saveSimulation(any(LoanRequest.class), any(LoanResponse.class));
    }

    @Test
    void testSaveSimulationInvalidData() throws Exception {
        // Given
        when(loanSimulationService.canSaveSimulation(any(LoanRequest.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/save-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSaveRequest)))
                .andExpect(status().isBadRequest());

        verify(loanSimulationService).canSaveSimulation(any(LoanRequest.class));
        verify(loanSimulationService, never()).saveSimulation(any(), any());
    }

    @Test
    void testSaveSimulationServiceException() throws Exception {
        // Given
        when(loanSimulationService.canSaveSimulation(any(LoanRequest.class))).thenReturn(true);
        when(loanSimulationService.saveSimulation(any(LoanRequest.class), any(LoanResponse.class)))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(post("/api/save-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSaveRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetSavedSimulationsSuccess() throws Exception {
        // Given
        List<LoanSimulation> simulations = Arrays.asList(savedSimulation);
        when(loanSimulationService.getAllSimulations()).thenReturn(simulations);

        // When & Then
        mockMvc.perform(get("/api/saved-simulations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clientName").value("Jean Dupont"));

        verify(loanSimulationService).getAllSimulations();
    }

    @Test
    void testGetSavedSimulationsWithClientNameFilter() throws Exception {
        // Given
        List<LoanSimulation> simulations = Arrays.asList(savedSimulation);
        when(loanSimulationService.searchByClientName("Jean")).thenReturn(simulations);

        // When & Then
        mockMvc.perform(get("/api/saved-simulations")
                .param("clientName", "Jean"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(loanSimulationService).searchByClientName("Jean");
        verify(loanSimulationService, never()).getAllSimulations();
    }

    @Test
    void testGetSavedSimulationsWithAgeCategoryFilter() throws Exception {
        // Given
        List<LoanSimulation> simulations = Arrays.asList(savedSimulation);
        when(loanSimulationService.getSimulationsByAgeCategory("ADULT")).thenReturn(simulations);

        // When & Then
        mockMvc.perform(get("/api/saved-simulations")
                .param("ageCategory", "ADULT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(loanSimulationService).getSimulationsByAgeCategory("ADULT");
    }

    @Test
    void testGetSavedSimulationsWithProfessionalCategoryFilter() throws Exception {
        // Given
        List<LoanSimulation> simulations = Arrays.asList(savedSimulation);
        when(loanSimulationService.getSimulationsByProfessionalCategory("EXECUTIVE")).thenReturn(simulations);

        // When & Then
        mockMvc.perform(get("/api/saved-simulations")
                .param("professionalCategory", "EXECUTIVE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(loanSimulationService).getSimulationsByProfessionalCategory("EXECUTIVE");
    }

    @Test
    void testGetSimulationByIdSuccess() throws Exception {
        // Given
        when(loanSimulationService.getSimulationById(1L)).thenReturn(Optional.of(savedSimulation));

        // When & Then
        mockMvc.perform(get("/api/saved-simulations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jean"))
                .andExpect(jsonPath("$.lastName").value("Dupont"));

        verify(loanSimulationService).getSimulationById(1L);
    }

    @Test
    void testGetSimulationByIdNotFound() throws Exception {
        // Given
        when(loanSimulationService.getSimulationById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/saved-simulations/999"))
                .andExpect(status().isNotFound());

        verify(loanSimulationService).getSimulationById(999L);
    }

    @Test
    void testDeleteSimulationSuccess() throws Exception {
        // Given
        when(loanSimulationService.deleteSimulation(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/saved-simulations/1"))
                .andExpect(status().isOk());

        verify(loanSimulationService).deleteSimulation(1L);
    }

    @Test
    void testDeleteSimulationNotFound() throws Exception {
        // Given
        when(loanSimulationService.deleteSimulation(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/saved-simulations/999"))
                .andExpect(status().isNotFound());

        verify(loanSimulationService).deleteSimulation(999L);
    }

    @Test
    void testGetSimulationsCount() throws Exception {
        // Given
        when(loanSimulationService.getTotalSimulationsCount()).thenReturn(42L);

        // When & Then
        mockMvc.perform(get("/api/simulations-count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("42"));

        verify(loanSimulationService).getTotalSimulationsCount();
    }

    @Test
    void testGetSavedSimulationsServiceException() throws Exception {
        // Given
        when(loanSimulationService.getAllSimulations()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/saved-simulations"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteSimulationServiceException() throws Exception {
        // Given
        when(loanSimulationService.deleteSimulation(1L)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(delete("/api/saved-simulations/1"))
                .andExpect(status().isInternalServerError());
    }
}