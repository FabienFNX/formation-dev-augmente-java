package com.example.loansimulator.dto;

public class InterestRateResponse {
    private double annualInterestRate;
    private double baseRate;
    private double ageModifier;
    private double professionalModifier;
    private double incomeModifier;
    private String ageCategory;
    private String professionalCategory;
    private double monthlyNetIncome;

    public InterestRateResponse() {}

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

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }

    public String getProfessionalCategory() {
        return professionalCategory;
    }

    public void setProfessionalCategory(String professionalCategory) {
        this.professionalCategory = professionalCategory;
    }

    public double getMonthlyNetIncome() {
        return monthlyNetIncome;
    }

    public void setMonthlyNetIncome(double monthlyNetIncome) {
        this.monthlyNetIncome = monthlyNetIncome;
    }
}