package com.example.loansimulator.model;

public class LoanResponse {
    private double loanAmount;
    private double totalInterest;
    private double totalCost;
    private double monthlyPayment;
    private int durationYears;
    private double annualInterestRate;

    public LoanResponse() {}

    public LoanResponse(double loanAmount, double totalInterest, double totalCost,
                       double monthlyPayment, int durationYears, double annualInterestRate) {
        this.loanAmount = loanAmount;
        this.totalInterest = totalInterest;
        this.totalCost = totalCost;
        this.monthlyPayment = monthlyPayment;
        this.durationYears = durationYears;
        this.annualInterestRate = annualInterestRate;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
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
}