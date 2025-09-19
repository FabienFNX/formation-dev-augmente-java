package com.example.loansimulator.model;

public class LoanRequest {
    private double amount;
    private int durationYears;
    private Double annualInterestRate; // Optionnel - calculé automatiquement si null

    // Nouveaux champs pour l'interface employé
    private String firstName;
    private String lastName;
    private String ageCategory;
    private String professionalCategory;
    private Double monthlyNetIncome;

    public LoanRequest() {}

    public LoanRequest(double amount, int durationYears, Double annualInterestRate) {
        this.amount = amount;
        this.durationYears = durationYears;
        this.annualInterestRate = annualInterestRate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    public Double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(Double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Double getMonthlyNetIncome() {
        return monthlyNetIncome;
    }

    public void setMonthlyNetIncome(Double monthlyNetIncome) {
        this.monthlyNetIncome = monthlyNetIncome;
    }
}