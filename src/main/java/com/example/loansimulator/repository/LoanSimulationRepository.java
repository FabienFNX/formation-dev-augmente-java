package com.example.loansimulator.repository;

import com.example.loansimulator.entity.LoanSimulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanSimulationRepository extends JpaRepository<LoanSimulation, Long> {

    /**
     * Récupère toutes les simulations triées par date de création (plus récentes en premier)
     */
    List<LoanSimulation> findAllByOrderByCreatedAtDesc();

    /**
     * Recherche les simulations par nom de client (insensible à la casse)
     */
    @Query("SELECT ls FROM LoanSimulation ls WHERE " +
           "LOWER(ls.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
           "LOWER(ls.lastName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<LoanSimulation> findByClientNameContainingIgnoreCase(String name);

    /**
     * Récupère les simulations créées entre deux dates
     */
    List<LoanSimulation> findByCreatedAtBetweenOrderByCreatedAtDesc(
        LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Compte le nombre total de simulations
     */
    long count();

    /**
     * Récupère les simulations par catégorie d'âge
     */
    List<LoanSimulation> findByAgeCategoryOrderByCreatedAtDesc(String ageCategory);

    /**
     * Récupère les simulations par catégorie professionnelle
     */
    List<LoanSimulation> findByProfessionalCategoryOrderByCreatedAtDesc(String professionalCategory);
}