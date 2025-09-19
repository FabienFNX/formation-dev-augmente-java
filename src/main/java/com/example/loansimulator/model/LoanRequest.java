package com.example.loansimulator.model;

public class LoanRequest {
    private double amount;
    private int durationYears;
    private double annualInterestRate;

    public LoanRequest() {}

    public LoanRequest(double amount, int durationYears, double annualInterestRate) {
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

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }
}