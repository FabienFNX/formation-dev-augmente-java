package com.example.interestrateapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Catégories d'âge pour le calcul du taux d'intérêt")
public enum AgeCategory {

    @Schema(description = "Jeune adulte (18-30 ans) - Taux majoré")
    YOUNG_ADULT("18-30 ans", "Jeune adulte", 0.2),

    @Schema(description = "Adulte (31-45 ans) - Taux de référence")
    ADULT("31-45 ans", "Adulte", 0.0),

    @Schema(description = "Âge moyen (46-60 ans) - Taux préférentiel")
    MIDDLE_AGED("46-60 ans", "Âge moyen", -0.1),

    @Schema(description = "Senior (61+ ans) - Taux majoré")
    SENIOR("61+ ans", "Senior", 0.3);

    private final String ageRange;
    private final String description;
    private final double rateModifier;

    AgeCategory(String ageRange, String description, double rateModifier) {
        this.ageRange = ageRange;
        this.description = description;
        this.rateModifier = rateModifier;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getDescription() {
        return description;
    }

    public double getRateModifier() {
        return rateModifier;
    }
}