package com.example.loansimulator.controller;

import com.example.loansimulator.dto.LoanSimulationSummary;
import com.example.loansimulator.dto.SaveSimulationRequest;
import com.example.loansimulator.entity.LoanSimulation;
import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import com.example.loansimulator.service.LoanCalculationService;
import com.example.loansimulator.service.LoanSimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    private LoanCalculationService loanCalculationService;

    @Autowired
    private LoanSimulationService loanSimulationService;

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

    @PostMapping("/save-simulation")
    public ResponseEntity<LoanSimulationSummary> saveSimulation(@RequestBody SaveSimulationRequest request) {
        try {
            logger.info("Received save simulation request for client: {} {}",
                request.getLoanRequest().getFirstName(), request.getLoanRequest().getLastName());

            // Vérifier que la simulation peut être sauvegardée (données employé présentes)
            if (!loanSimulationService.canSaveSimulation(request.getLoanRequest())) {
                logger.warn("Cannot save simulation: missing employee data");
                return ResponseEntity.badRequest().build();
            }

            LoanSimulation savedSimulation = loanSimulationService.saveSimulation(
                request.getLoanRequest(), request.getLoanResponse());

            LoanSimulationSummary summary = new LoanSimulationSummary(savedSimulation);
            logger.info("Simulation saved successfully with ID: {}", savedSimulation.getId());

            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error saving simulation: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/saved-simulations")
    public ResponseEntity<List<LoanSimulationSummary>> getSavedSimulations(
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String ageCategory,
            @RequestParam(required = false) String professionalCategory) {
        try {
            logger.info("Fetching saved simulations with filters: clientName={}, ageCategory={}, professionalCategory={}",
                clientName, ageCategory, professionalCategory);

            List<LoanSimulation> simulations;

            if (clientName != null && !clientName.trim().isEmpty()) {
                simulations = loanSimulationService.searchByClientName(clientName.trim());
            } else if (ageCategory != null && !ageCategory.trim().isEmpty()) {
                simulations = loanSimulationService.getSimulationsByAgeCategory(ageCategory.trim());
            } else if (professionalCategory != null && !professionalCategory.trim().isEmpty()) {
                simulations = loanSimulationService.getSimulationsByProfessionalCategory(professionalCategory.trim());
            } else {
                simulations = loanSimulationService.getAllSimulations();
            }

            List<LoanSimulationSummary> summaries = simulations.stream()
                .map(LoanSimulationSummary::new)
                .toList();

            logger.info("Found {} saved simulations", summaries.size());
            return ResponseEntity.ok(summaries);
        } catch (Exception e) {
            logger.error("Error fetching saved simulations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/saved-simulations/{id}")
    public ResponseEntity<LoanSimulation> getSimulationById(@PathVariable Long id) {
        try {
            logger.info("Fetching simulation with ID: {}", id);

            return loanSimulationService.getSimulationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error fetching simulation by ID: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/saved-simulations/{id}")
    public ResponseEntity<Void> deleteSimulation(@PathVariable Long id) {
        try {
            logger.info("Deleting simulation with ID: {}", id);

            boolean deleted = loanSimulationService.deleteSimulation(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting simulation: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/simulations-count")
    public ResponseEntity<Long> getSimulationsCount() {
        try {
            long count = loanSimulationService.getTotalSimulationsCount();
            logger.info("Total simulations count: {}", count);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting simulations count: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}