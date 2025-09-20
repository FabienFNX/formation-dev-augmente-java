package com.example.interestrateapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informations sur une catégorie")
public class CategoryInfo {

    @Schema(description = "Code de la catégorie", example = "ADULT")
    private String code;

    @Schema(description = "Nom de la catégorie", example = "Adulte")
    private String name;

    @Schema(description = "Description de la catégorie", example = "31-45 ans")
    private String description;

    @Schema(description = "Modificateur de taux appliqué", example = "0.0")
    private double rateModifier;

    public CategoryInfo() {}

    public CategoryInfo(String code, String name, String description, double rateModifier) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.rateModifier = rateModifier;
    }

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