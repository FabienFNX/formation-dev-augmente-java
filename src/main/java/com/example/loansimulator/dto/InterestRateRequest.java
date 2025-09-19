package com.example.loansimulator.dto;

public class InterestRateRequest {
    private String ageCategory;
    private String professionalCategory;
    private Double monthlyNetIncome;

    public InterestRateRequest() {}

    public InterestRateRequest(String ageCategory, String professionalCategory, Double monthlyNetIncome) {
        this.ageCategory = ageCategory;
        this.professionalCategory = professionalCategory;
        this.monthlyNetIncome = monthlyNetIncome;
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