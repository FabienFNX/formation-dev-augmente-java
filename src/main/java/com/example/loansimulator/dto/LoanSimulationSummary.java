package com.example.loansimulator.dto;

import com.example.loansimulator.entity.LoanSimulation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoanSimulationSummary {
    private Long id;
    private String clientName;
    private double loanAmount;
    private int durationYears;
    private double annualInterestRate;
    private double monthlyPayment;
    private double totalCost;
    private String ageCategory;
    private String professionalCategory;
    private Double monthlyNetIncome;
    private LocalDateTime createdAt;
    private String formattedCreatedAt;

    public LoanSimulationSummary() {}

    public LoanSimulationSummary(LoanSimulation simulation) {
        this.id = simulation.getId();
        this.clientName = formatClientName(simulation.getFirstName(), simulation.getLastName());
        this.loanAmount = simulation.getLoanAmount();
        this.durationYears = simulation.getDurationYears();
        this.annualInterestRate = simulation.getAnnualInterestRate();
        this.monthlyPayment = simulation.getMonthlyPayment();
        this.totalCost = simulation.getTotalCost();
        this.ageCategory = simulation.getAgeCategory();
        this.professionalCategory = simulation.getProfessionalCategory();
        this.monthlyNetIncome = simulation.getMonthlyNetIncome();
        this.createdAt = simulation.getCreatedAt();
        this.formattedCreatedAt = formatDateTime(simulation.getCreatedAt());
    }

    private String formatClientName(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        } else {
            return "Client anonyme";
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return dateTime.format(formatter);
        }
        return "";
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }

    public void setFormattedCreatedAt(String formattedCreatedAt) {
        this.formattedCreatedAt = formattedCreatedAt;
    }
}