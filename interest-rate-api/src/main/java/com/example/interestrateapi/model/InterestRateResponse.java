package com.example.interestrateapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse du calcul de taux d'intérêt")
public class InterestRateResponse {

    @Schema(description = "Taux d'intérêt annuel calculé en pourcentage", example = "1.7")
    private double annualInterestRate;

    @Schema(description = "Taux de base utilisé", example = "1.5")
    private double baseRate;

    @Schema(description = "Modificateur appliqué pour l'âge", example = "0.2")
    private double ageModifier;

    @Schema(description = "Modificateur appliqué pour la catégorie professionnelle", example = "0.0")
    private double professionalModifier;

    @Schema(description = "Modificateur appliqué pour le revenu", example = "0.0")
    private double incomeModifier;

    @Schema(description = "Catégorie d'âge utilisée", example = "ADULT")
    private AgeCategory ageCategory;

    @Schema(description = "Catégorie professionnelle utilisée", example = "EMPLOYEE")
    private ProfessionalCategory professionalCategory;

    @Schema(description = "Revenu mensuel net considéré", example = "3500.0")
    private double monthlyNetIncome;

    public InterestRateResponse() {}

    public InterestRateResponse(double annualInterestRate, double baseRate, double ageModifier,
                               double professionalModifier, double incomeModifier,
                               AgeCategory ageCategory, ProfessionalCategory professionalCategory,
                               double monthlyNetIncome) {
        this.annualInterestRate = annualInterestRate;
        this.baseRate = baseRate;
        this.ageModifier = ageModifier;
        this.professionalModifier = professionalModifier;
        this.incomeModifier = incomeModifier;
        this.ageCategory = ageCategory;
        this.professionalCategory = professionalCategory;
        this.monthlyNetIncome = monthlyNetIncome;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getAgeModifier() {
        return ageModifier;
    }

    public void setAgeModifier(double ageModifier) {
        this.ageModifier = ageModifier;
    }

    public double getProfessionalModifier() {
        return professionalModifier;
    }

    public void setProfessionalModifier(double professionalModifier) {
        this.professionalModifier = professionalModifier;
    }

    public double getIncomeModifier() {
        return incomeModifier;
    }

    public void setIncomeModifier(double incomeModifier) {
        this.incomeModifier = incomeModifier;
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

    public double getMonthlyNetIncome() {
        return monthlyNetIncome;
    }

    public void setMonthlyNetIncome(double monthlyNetIncome) {
        this.monthlyNetIncome = monthlyNetIncome;
    }
}