package com.example.loansimulator.dto;

public class CategoryInfo {
    private String code;
    private String name;
    private String description;
    private double rateModifier;

    public CategoryInfo() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRateModifier() {
        return rateModifier;
    }

    public void setRateModifier(double rateModifier) {
        this.rateModifier = rateModifier;
    }
}