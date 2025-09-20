package com.example.interestrateapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Demande de calcul de taux d'intérêt")
public class InterestRateRequest {

    @Schema(description = "Catégorie d'âge du demandeur", example = "ADULT", required = true)
    @NotNull(message = "La catégorie d'âge est obligatoire")
    private AgeCategory ageCategory;

    @Schema(description = "Catégorie socio-professionnelle", example = "EMPLOYEE", required = true)
    @NotNull(message = "La catégorie professionnelle est obligatoire")
    private ProfessionalCategory professionalCategory;

    @Schema(description = "Revenu mensuel net en euros", example = "3500.0", required = true)
    @NotNull(message = "Le revenu mensuel est obligatoire")
    @Positive(message = "Le revenu mensuel doit être positif")
    private Double monthlyNetIncome;

    public InterestRateRequest() {}

    public InterestRateRequest(AgeCategory ageCategory, ProfessionalCategory professionalCategory, Double monthlyNetIncome) {
        this.ageCategory = ageCategory;
        this.professionalCategory = professionalCategory;
        this.monthlyNetIncome = monthlyNetIncome;
    }

    public AgeCategory getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(AgeCategory ageCategory) {
        this.ageCategory = ageCategory;
    }

    public ProfessionalCategory getProfessionalCategory() {
        return professionalCategory;
    }

    public void setProfessionalCategory(ProfessionalCategory professionalCategory) {
        this.professionalCategory = professionalCategory;
    }

    public Double getMonthlyNetIncome() {
        return monthlyNetIncome;
    }

    public void setMonthlyNetIncome(Double monthlyNetIncome) {
        this.monthlyNetIncome = monthlyNetIncome;
    }
}