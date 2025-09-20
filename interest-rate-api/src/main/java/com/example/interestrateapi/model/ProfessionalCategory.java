package com.example.interestrateapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Catégories socio-professionnelles pour le calcul du taux d'intérêt")
public enum ProfessionalCategory {

    @Schema(description = "Salarié en CDI - Taux de référence")
    EMPLOYEE("Salarié CDI", "Employé en contrat à durée indéterminée", 0.0),

    @Schema(description = "Cadre - Taux préférentiel")
    EXECUTIVE("Cadre", "Cadre dirigeant ou ingénieur", -0.2),

    @Schema(description = "Fonctionnaire - Taux très préférentiel")
    CIVIL_SERVANT("Fonctionnaire", "Agent de la fonction publique", -0.3),

    @Schema(description = "Indépendant/Freelancer - Taux majoré")
    FREELANCER("Indépendant", "Travailleur indépendant ou freelance", 0.4),

    @Schema(description = "Retraité - Taux légèrement majoré")
    RETIRED("Retraité", "Personne à la retraite", 0.1),

    @Schema(description = "Étudiant - Taux fortement majoré")
    STUDENT("Étudiant", "Étudiant ou apprenti", 0.5),

    @Schema(description = "Sans emploi - Taux très majoré")
    UNEMPLOYED("Sans emploi", "Personne sans activité professionnelle", 0.8);

    private final String name;
    private final String description;
    private final double rateModifier;

    ProfessionalCategory(String name, String description, double rateModifier) {
        this.name = name;
        this.description = description;
        this.rateModifier = rateModifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getRateModifier() {
        return rateModifier;
    }
}