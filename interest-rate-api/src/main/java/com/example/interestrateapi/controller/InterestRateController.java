package com.example.interestrateapi.controller;

import com.example.interestrateapi.config.InterestRateConfig;
import com.example.interestrateapi.model.*;
import com.example.interestrateapi.service.InterestRateCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interest-rate")
@CrossOrigin(origins = "*")
@Tag(name = "Interest Rate API", description = "API de calcul de taux d'intérêt basé sur des critères sociodémographiques")
public class InterestRateController {

    @Autowired
    private InterestRateCalculationService interestRateService;

    @Operation(
            summary = "Calculer le taux d'intérêt",
            description = "Calcule le taux d'intérêt annuel en fonction de l'âge, de la catégorie professionnelle et du revenu mensuel"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Taux d'intérêt calculé avec succès",
                    content = @Content(schema = @Schema(implementation = InterestRateResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données d'entrée invalides"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur interne du serveur"
            )
    })
    @PostMapping("/calculate")
    public ResponseEntity<InterestRateResponse> calculateInterestRate(
            @Parameter(description = "Données pour le calcul du taux d'intérêt", required = true)
            @Valid @RequestBody InterestRateRequest request) {
        try {
            InterestRateResponse response = interestRateService.calculateInterestRate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Obtenir les catégories d'âge",
            description = "Retourne la liste de toutes les catégories d'âge disponibles avec leurs modificateurs"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des catégories d'âge récupérée avec succès"
    )
    @GetMapping("/categories/age")
    public ResponseEntity<List<CategoryInfo>> getAgeCategories() {
        return ResponseEntity.ok(interestRateService.getAgeCategories());
    }

    @Operation(
            summary = "Obtenir les catégories professionnelles",
            description = "Retourne la liste de toutes les catégories socio-professionnelles avec leurs modificateurs"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des catégories professionnelles récupérée avec succès"
    )
    @GetMapping("/categories/professional")
    public ResponseEntity<List<CategoryInfo>> getProfessionalCategories() {
        return ResponseEntity.ok(interestRateService.getProfessionalCategories());
    }

    @Operation(
            summary = "Obtenir la configuration actuelle",
            description = "Retourne la configuration actuelle des taux et seuils utilisés pour les calculs"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Configuration récupérée avec succès"
    )
    @GetMapping("/config")
    public ResponseEntity<InterestRateConfig> getCurrentConfig() {
        return ResponseEntity.ok(interestRateService.getCurrentConfig());
    }
}