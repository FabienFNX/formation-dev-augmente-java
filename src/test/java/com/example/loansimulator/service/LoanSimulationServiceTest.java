package com.example.loansimulator.service;

import com.example.loansimulator.entity.LoanSimulation;
import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import com.example.loansimulator.repository.LoanSimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanSimulationServiceTest {

    @Mock
    private LoanSimulationRepository loanSimulationRepository;

    @InjectMocks
    private LoanSimulationService loanSimulationService;

    private LoanRequest validRequest;
    private LoanResponse validResponse;
    private LoanSimulation savedSimulation;

    @BeforeEach
    void setUp() {
        validRequest = new LoanRequest();
        validRequest.setFirstName("Jean");
        validRequest.setLastName("Dupont");
        validRequest.setAgeCategory("ADULT");
        validRequest.setProfessionalCategory("EXECUTIVE");
        validRequest.setMonthlyNetIncome(4500.0);
        validRequest.setAmount(200000.0);
        validRequest.setDurationYears(20);

        validResponse = new LoanResponse(
            200000.0, 54353.39, 254353.39, 1059.81, 20, 2.5
        );

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
    }

    @Test
    void testSaveSimulation() {
        // Given
        when(loanSimulationRepository.save(any(LoanSimulation.class))).thenReturn(savedSimulation);

        // When
        LoanSimulation result = loanSimulationService.saveSimulation(validRequest, validResponse);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jean", result.getFirstName());
        assertEquals("Dupont", result.getLastName());
        assertEquals("ADULT", result.getAgeCategory());
        assertEquals("EXECUTIVE", result.getProfessionalCategory());
        assertEquals(4500.0, result.getMonthlyNetIncome());
        assertEquals(200000.0, result.getLoanAmount());
        assertEquals(20, result.getDurationYears());
        assertEquals(2.5, result.getAnnualInterestRate());

        verify(loanSimulationRepository).save(any(LoanSimulation.class));
    }

    @Test
    void testGetAllSimulations() {
        // Given
        List<LoanSimulation> mockSimulations = Arrays.asList(savedSimulation);
        when(loanSimulationRepository.findAllByOrderByCreatedAtDesc()).thenReturn(mockSimulations);

        // When
        List<LoanSimulation> result = loanSimulationService.getAllSimulations();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedSimulation, result.get(0));

        verify(loanSimulationRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void testGetSimulationById() {
        // Given
        when(loanSimulationRepository.findById(1L)).thenReturn(Optional.of(savedSimulation));

        // When
        Optional<LoanSimulation> result = loanSimulationService.getSimulationById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(savedSimulation, result.get());

        verify(loanSimulationRepository).findById(1L);
    }

    @Test
    void testGetSimulationByIdNotFound() {
        // Given
        when(loanSimulationRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<LoanSimulation> result = loanSimulationService.getSimulationById(999L);

        // Then
        assertFalse(result.isPresent());

        verify(loanSimulationRepository).findById(999L);
    }

    @Test
    void testSearchByClientName() {
        // Given
        List<LoanSimulation> mockSimulations = Arrays.asList(savedSimulation);
        when(loanSimulationRepository.findByClientNameContainingIgnoreCase("Jean"))
            .thenReturn(mockSimulations);

        // When
        List<LoanSimulation> result = loanSimulationService.searchByClientName("Jean");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedSimulation, result.get(0));

        verify(loanSimulationRepository).findByClientNameContainingIgnoreCase("Jean");
    }

    @Test
    void testDeleteSimulationSuccess() {
        // Given
        when(loanSimulationRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = loanSimulationService.deleteSimulation(1L);

        // Then
        assertTrue(result);

        verify(loanSimulationRepository).existsById(1L);
        verify(loanSimulationRepository).deleteById(1L);
    }

    @Test
    void testDeleteSimulationNotFound() {
        // Given
        when(loanSimulationRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = loanSimulationService.deleteSimulation(999L);

        // Then
        assertFalse(result);

        verify(loanSimulationRepository).existsById(999L);
        verify(loanSimulationRepository, never()).deleteById(any());
    }

    @Test
    void testGetTotalSimulationsCount() {
        // Given
        when(loanSimulationRepository.count()).thenReturn(42L);

        // When
        long result = loanSimulationService.getTotalSimulationsCount();

        // Then
        assertEquals(42L, result);

        verify(loanSimulationRepository).count();
    }

    @Test
    void testCanSaveSimulationValid() {
        // When
        boolean result = loanSimulationService.canSaveSimulation(validRequest);

        // Then
        assertTrue(result);
    }

    @Test
    void testCanSaveSimulationMissingFirstName() {
        // Given
        validRequest.setFirstName(null);

        // When
        boolean result = loanSimulationService.canSaveSimulation(validRequest);

        // Then
        assertFalse(result);
    }

    @Test
    void testCanSaveSimulationEmptyFirstName() {
        // Given
        validRequest.setFirstName("   ");

        // When
        boolean result = loanSimulationService.canSaveSimulation(validRequest);

        // Then
        assertFalse(result);
    }

    @Test
    void testCanSaveSimulationMissingLastName() {
        // Given
        validRequest.setLastName(null);

        // When
        boolean result = loanSimulationService.canSaveSimulation(validRequest);

        // Then
        assertFalse(result);
    }

    @Test
    void testCanSaveSimulationMissingAgeCategory() {
        // Given
        validRequest.setAgeCategory(null);

        // When
        boolean result = loanSimulationService.canSaveSimulation(validRequest);

        // Then
        assertFalse(result);
    }

    @Test
    void testCanSaveSimulationMissingProfessionalCategory() {
        // Given
        validRequest.setProfessionalCategory(null);

        // When
        boolean result = loanSimulationService.canSaveSimulation(validRequest);

        // Then
        assertFalse(result);
    }

    @Test
    void testCanSaveSimulationMissingIncome() {
        // Given
        validRequest.setMonthlyNetIncome(null);

        // When
        boolean result = loanSimulationService.canSaveSimulation(validRequest);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetSimulationsByAgeCategory() {
        // Given
        List<LoanSimulation> mockSimulations = Arrays.asList(savedSimulation);
        when(loanSimulationRepository.findByAgeCategoryOrderByCreatedAtDesc("ADULT"))
            .thenReturn(mockSimulations);

        // When
        List<LoanSimulation> result = loanSimulationService.getSimulationsByAgeCategory("ADULT");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedSimulation, result.get(0));

        verify(loanSimulationRepository).findByAgeCategoryOrderByCreatedAtDesc("ADULT");
    }

    @Test
    void testGetSimulationsByProfessionalCategory() {
        // Given
        List<LoanSimulation> mockSimulations = Arrays.asList(savedSimulation);
        when(loanSimulationRepository.findByProfessionalCategoryOrderByCreatedAtDesc("EXECUTIVE"))
            .thenReturn(mockSimulations);

        // When
        List<LoanSimulation> result = loanSimulationService.getSimulationsByProfessionalCategory("EXECUTIVE");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedSimulation, result.get(0));

        verify(loanSimulationRepository).findByProfessionalCategoryOrderByCreatedAtDesc("EXECUTIVE");
    }
}