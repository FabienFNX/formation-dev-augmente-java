package com.example.loansimulator.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_simulations")
public class LoanSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Données client
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age_category")
    private String ageCategory;

    @Column(name = "professional_category")
    private String professionalCategory;

    @Column(name = "monthly_net_income")
    private Double monthlyNetIncome;

    // Paramètres du prêt
    @Column(name = "loan_amount", nullable = false)
    private double loanAmount;

    @Column(name = "duration_years", nullable = false)
    private int durationYears;

    @Column(name = "annual_interest_rate", nullable = false)
    private double annualInterestRate;

    // Résultats de la simulation
    @Column(name = "total_interest", nullable = false)
    private double totalInterest;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;

    @Column(name = "monthly_payment", nullable = false)
    private double monthlyPayment;

    // Métadonnées
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructeurs
    public LoanSimulation() {}

    public LoanSimulation(String firstName, String lastName, String ageCategory,
                         String professionalCategory, Double monthlyNetIncome,
                         double loanAmount, int durationYears, double annualInterestRate,
                         double totalInterest, double totalCost, double monthlyPayment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ageCategory = ageCategory;
        this.professionalCategory = professionalCategory;
        this.monthlyNetIncome = monthlyNetIncome;
        this.loanAmount = loanAmount;
        this.durationYears = durationYears;
        this.annualInterestRate = annualInterestRate;
        this.totalInterest = totalInterest;
        this.totalCost = totalCost;
        this.monthlyPayment = monthlyPayment;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}