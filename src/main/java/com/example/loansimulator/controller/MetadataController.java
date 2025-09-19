package com.example.loansimulator.controller;

import com.example.loansimulator.dto.CategoryInfo;
import com.example.loansimulator.service.InterestRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MetadataController {

    @Autowired
    private InterestRateService interestRateService;

    @GetMapping("/age-categories")
    public ResponseEntity<List<CategoryInfo>> getAgeCategories() {
        try {
            List<CategoryInfo> categories = interestRateService.getAgeCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/professional-categories")
    public ResponseEntity<List<CategoryInfo>> getProfessionalCategories() {
        try {
            List<CategoryInfo> categories = interestRateService.getProfessionalCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}