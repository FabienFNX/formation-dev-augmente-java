package com.example.loansimulator.service;

import com.example.loansimulator.entity.LoanSimulation;
import com.example.loansimulator.model.LoanRequest;
import com.example.loansimulator.model.LoanResponse;
import com.example.loansimulator.repository.LoanSimulationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanSimulationService {

    private static final Logger logger = LoggerFactory.getLogger(LoanSimulationService.class);

    @Autowired
    private LoanSimulationRepository loanSimulationRepository;

    /**
     * Sauvegarde une simulation de prêt en base de données
     */
    public LoanSimulation saveSimulation(LoanRequest request, LoanResponse response) {
        logger.info("Saving loan simulation for client: {} {}", request.getFirstName(), request.getLastName());

        LoanSimulation simulation = new LoanSimulation(
            request.getFirstName(),
            request.getLastName(),
            request.getAgeCategory(),
            request.getProfessionalCategory(),
            request.getMonthlyNetIncome(),
            response.getLoanAmount(),
            response.getDurationYears(),
            response.getAnnualInterestRate(),
            response.getTotalInterest(),
            response.getTotalCost(),
            response.getMonthlyPayment()
        );

        LoanSimulation savedSimulation = loanSimulationRepository.save(simulation);
        logger.info("Loan simulation saved with ID: {}", savedSimulation.getId());

        return savedSimulation;
    }

    /**
     * Récupère toutes les simulations triées par date (plus récentes en premier)
     */
    public List<LoanSimulation> getAllSimulations() {
        logger.info("Fetching all loan simulations");
        return loanSimulationRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Récupère une simulation par son ID
     */
    public Optional<LoanSimulation> getSimulationById(Long id) {
        logger.info("Fetching loan simulation with ID: {}", id);
        return loanSimulationRepository.findById(id);
    }

    /**
     * Recherche les simulations par nom de client
     */
    public List<LoanSimulation> searchByClientName(String name) {
        logger.info("Searching loan simulations for client name: {}", name);
        return loanSimulationRepository.findByClientNameContainingIgnoreCase(name);
    }

    /**
     * Récupère les simulations créées entre deux dates
     */
    public List<LoanSimulation> getSimulationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching loan simulations between {} and {}", startDate, endDate);
        return loanSimulationRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate);
    }

    /**
     * Récupère les simulations par catégorie d'âge
     */
    public List<LoanSimulation> getSimulationsByAgeCategory(String ageCategory) {
        logger.info("Fetching loan simulations for age category: {}", ageCategory);
        return loanSimulationRepository.findByAgeCategoryOrderByCreatedAtDesc(ageCategory);
    }

    /**
     * Récupère les simulations par catégorie professionnelle
     */
    public List<LoanSimulation> getSimulationsByProfessionalCategory(String professionalCategory) {
        logger.info("Fetching loan simulations for professional category: {}", professionalCategory);
        return loanSimulationRepository.findByProfessionalCategoryOrderByCreatedAtDesc(professionalCategory);
    }

    /**
     * Supprime une simulation par son ID
     */
    public boolean deleteSimulation(Long id) {
        logger.info("Deleting loan simulation with ID: {}", id);
        if (loanSimulationRepository.existsById(id)) {
            loanSimulationRepository.deleteById(id);
            logger.info("Loan simulation with ID {} deleted successfully", id);
            return true;
        } else {
            logger.warn("Loan simulation with ID {} not found for deletion", id);
            return false;
        }
    }

    /**
     * Compte le nombre total de simulations
     */
    public long getTotalSimulationsCount() {
        return loanSimulationRepository.count();
    }

    /**
     * Vérifie si une simulation peut être sauvegardée (données employé présentes)
     */
    public boolean canSaveSimulation(LoanRequest request) {
        return request.getFirstName() != null && !request.getFirstName().trim().isEmpty() &&
               request.getLastName() != null && !request.getLastName().trim().isEmpty() &&
               request.getAgeCategory() != null &&
               request.getProfessionalCategory() != null &&
               request.getMonthlyNetIncome() != null;
    }

    /**
     * Supprime toutes les simulations
     */
    public void deleteAllSimulations() {
        logger.info("Deleting all loan simulations");
        loanSimulationRepository.deleteAll();
        logger.info("All loan simulations deleted successfully");
    }

    /**
     * Crée des données d'exemple pour démonstration
     */
    public void createSampleData() {
        logger.info("Creating sample loan simulations");

        // Création des simulations d'exemple
        LoanSimulation[] sampleSimulations = {
            new LoanSimulation("Marie", "Dubois", "YOUNG", "EXECUTIVE", 4500.0, 250000.0, 25, 2.8, 87654.32, 337654.32, 1125.81),
            new LoanSimulation("Jean", "Martin", "ADULT", "EXECUTIVE", 6500.0, 400000.0, 20, 2.5, 105263.16, 505263.16, 2105.26),
            new LoanSimulation("Sophie", "Bernard", "YOUNG", "EMPLOYEE", 2800.0, 180000.0, 25, 3.2, 72840.96, 252840.96, 844.47),
            new LoanSimulation("Pierre", "Moreau", "SENIOR", "EXECUTIVE", 5200.0, 320000.0, 15, 2.3, 59904.61, 379904.61, 2110.59),
            new LoanSimulation("Isabelle", "Petit", "ADULT", "EMPLOYEE", 3200.0, 200000.0, 22, 3.0, 70909.09, 270909.09, 1030.67),
            new LoanSimulation("Thomas", "Durand", "YOUNG", "EXECUTIVE", 4200.0, 230000.0, 25, 2.9, 82045.45, 312045.45, 1040.15),
            new LoanSimulation("Catherine", "Leroy", "SENIOR", "EMPLOYEE", 3800.0, 220000.0, 18, 2.8, 58181.82, 278181.82, 1287.84),
            new LoanSimulation("Nicolas", "Roux", "ADULT", "EXECUTIVE", 5500.0, 350000.0, 20, 2.6, 94736.84, 444736.84, 1853.07),
            new LoanSimulation("Julie", "Vincent", "YOUNG", "EMPLOYEE", 2900.0, 160000.0, 25, 3.3, 66181.82, 226181.82, 754.61),
            new LoanSimulation("Alain", "Fournier", "SENIOR", "EXECUTIVE", 7200.0, 450000.0, 18, 2.4, 103636.36, 553636.36, 2575.76),
            new LoanSimulation("Céline", "Michel", "ADULT", "EMPLOYEE", 3400.0, 190000.0, 20, 3.1, 61052.63, 251052.63, 1046.05),
            new LoanSimulation("Laurent", "Garcia", "YOUNG", "EXECUTIVE", 4800.0, 280000.0, 22, 2.7, 84363.64, 364363.64, 1378.74),
            new LoanSimulation("Monique", "David", "SENIOR", "EMPLOYEE", 4000.0, 240000.0, 20, 2.9, 74526.32, 314526.32, 1309.68),
            new LoanSimulation("François", "Bertrand", "ADULT", "EXECUTIVE", 6000.0, 380000.0, 25, 2.6, 130526.32, 510526.32, 1701.75),
            new LoanSimulation("Sandrine", "Simon", "YOUNG", "EMPLOYEE", 3100.0, 170000.0, 23, 3.2, 60869.57, 230869.57, 834.42),
            new LoanSimulation("Gérard", "Laurent", "SENIOR", "EXECUTIVE", 8500.0, 500000.0, 15, 2.2, 87368.42, 587368.42, 3262.60),
            new LoanSimulation("Nathalie", "Lefebvre", "ADULT", "EMPLOYEE", 3600.0, 210000.0, 25, 3.0, 79736.84, 289736.84, 965.79),
            new LoanSimulation("Christophe", "Morel", "YOUNG", "EXECUTIVE", 4600.0, 260000.0, 20, 2.8, 76842.11, 336842.11, 1403.51),
            new LoanSimulation("Brigitte", "Andre", "SENIOR", "EMPLOYEE", 4200.0, 250000.0, 18, 2.7, 68421.05, 318421.05, 1480.10),
            new LoanSimulation("Stéphane", "Girard", "ADULT", "EXECUTIVE", 5800.0, 360000.0, 22, 2.5, 99473.68, 459473.68, 1737.77)
        };

        for (LoanSimulation simulation : sampleSimulations) {
            loanSimulationRepository.save(simulation);
        }

        logger.info("Sample loan simulations created successfully. Total count: {}", sampleSimulations.length);
    }
}