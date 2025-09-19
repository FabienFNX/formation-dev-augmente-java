package com.example.loansimulator.controller;

import com.example.loansimulator.dto.CategoryInfo;
import com.example.loansimulator.service.InterestRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetadataController.class)
class MetadataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterestRateService interestRateService;

    private CategoryInfo createCategoryInfo(String code, String name, String description, double rateModifier) {
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setCode(code);
        categoryInfo.setName(name);
        categoryInfo.setDescription(description);
        categoryInfo.setRateModifier(rateModifier);
        return categoryInfo;
    }

    @Test
    void testGetAgeCategoriesSuccess() throws Exception {
        // Given
        List<CategoryInfo> mockCategories = Arrays.asList(
            createCategoryInfo("YOUNG_ADULT", "Jeune adulte", "18-30 ans", 0.2),
            createCategoryInfo("ADULT", "Adulte", "31-45 ans", 0.0),
            createCategoryInfo("MIDDLE_AGED", "Âge moyen", "46-60 ans", -0.1),
            createCategoryInfo("SENIOR", "Senior", "61+ ans", 0.3)
        );

        when(interestRateService.getAgeCategories()).thenReturn(mockCategories);

        // When & Then
        mockMvc.perform(get("/api/age-categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].code").value("YOUNG_ADULT"))
                .andExpect(jsonPath("$[0].name").value("Jeune adulte"))
                .andExpect(jsonPath("$[0].description").value("18-30 ans"))
                .andExpect(jsonPath("$[0].rateModifier").value(0.2))
                .andExpect(jsonPath("$[1].code").value("ADULT"))
                .andExpect(jsonPath("$[1].rateModifier").value(0.0))
                .andExpect(jsonPath("$[2].code").value("MIDDLE_AGED"))
                .andExpect(jsonPath("$[2].rateModifier").value(-0.1))
                .andExpect(jsonPath("$[3].code").value("SENIOR"))
                .andExpect(jsonPath("$[3].rateModifier").value(0.3));

        verify(interestRateService).getAgeCategories();
    }

    @Test
    void testGetProfessionalCategoriesSuccess() throws Exception {
        // Given
        List<CategoryInfo> mockCategories = Arrays.asList(
            createCategoryInfo("EMPLOYEE", "Salarié CDI", "Employé en contrat à durée indéterminée", 0.0),
            createCategoryInfo("EXECUTIVE", "Cadre", "Cadre dirigeant ou ingénieur", -0.2),
            createCategoryInfo("CIVIL_SERVANT", "Fonctionnaire", "Agent de la fonction publique", -0.3),
            createCategoryInfo("FREELANCER", "Indépendant", "Travailleur indépendant ou freelance", 0.4)
        );

        when(interestRateService.getProfessionalCategories()).thenReturn(mockCategories);

        // When & Then
        mockMvc.perform(get("/api/professional-categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].code").value("EMPLOYEE"))
                .andExpect(jsonPath("$[0].name").value("Salarié CDI"))
                .andExpect(jsonPath("$[0].rateModifier").value(0.0))
                .andExpect(jsonPath("$[1].code").value("EXECUTIVE"))
                .andExpect(jsonPath("$[1].rateModifier").value(-0.2))
                .andExpect(jsonPath("$[2].code").value("CIVIL_SERVANT"))
                .andExpect(jsonPath("$[2].rateModifier").value(-0.3))
                .andExpect(jsonPath("$[3].code").value("FREELANCER"))
                .andExpect(jsonPath("$[3].rateModifier").value(0.4));

        verify(interestRateService).getProfessionalCategories();
    }

    @Test
    void testGetAgeCategoriesEmpty() throws Exception {
        // Given
        when(interestRateService.getAgeCategories()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/age-categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(interestRateService).getAgeCategories();
    }

    @Test
    void testGetProfessionalCategoriesEmpty() throws Exception {
        // Given
        when(interestRateService.getProfessionalCategories()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/professional-categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(interestRateService).getProfessionalCategories();
    }

    @Test
    void testGetAgeCategoriesWithServiceException() throws Exception {
        // Given
        when(interestRateService.getAgeCategories()).thenThrow(new RuntimeException("Service unavailable"));

        // When & Then
        mockMvc.perform(get("/api/age-categories"))
                .andExpect(status().isInternalServerError());

        verify(interestRateService).getAgeCategories();
    }

    @Test
    void testGetProfessionalCategoriesWithServiceException() throws Exception {
        // Given
        when(interestRateService.getProfessionalCategories()).thenThrow(new RuntimeException("Service unavailable"));

        // When & Then
        mockMvc.perform(get("/api/professional-categories"))
                .andExpect(status().isInternalServerError());

        verify(interestRateService).getProfessionalCategories();
    }

    @Test
    void testCorsHeaders() throws Exception {
        // Given
        List<CategoryInfo> mockCategories = Arrays.asList(
            createCategoryInfo("ADULT", "Adulte", "31-45 ans", 0.0)
        );

        when(interestRateService.getAgeCategories()).thenReturn(mockCategories);

        // When & Then
        mockMvc.perform(get("/api/age-categories")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    void testAgeCategoriesResponseStructure() throws Exception {
        // Given
        List<CategoryInfo> mockCategories = Arrays.asList(
            createCategoryInfo("TEST_CODE", "Test Name", "Test Description", 0.5)
        );

        when(interestRateService.getAgeCategories()).thenReturn(mockCategories);

        // When & Then
        mockMvc.perform(get("/api/age-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].rateModifier").exists())
                .andExpect(jsonPath("$[0].code").value("TEST_CODE"))
                .andExpect(jsonPath("$[0].name").value("Test Name"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].rateModifier").value(0.5));
    }

    @Test
    void testProfessionalCategoriesResponseStructure() throws Exception {
        // Given
        List<CategoryInfo> mockCategories = Arrays.asList(
            createCategoryInfo("TEST_PROF", "Test Professional", "Test Professional Description", -0.1)
        );

        when(interestRateService.getProfessionalCategories()).thenReturn(mockCategories);

        // When & Then
        mockMvc.perform(get("/api/professional-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].rateModifier").exists())
                .andExpect(jsonPath("$[0].code").value("TEST_PROF"))
                .andExpect(jsonPath("$[0].name").value("Test Professional"))
                .andExpect(jsonPath("$[0].description").value("Test Professional Description"))
                .andExpect(jsonPath("$[0].rateModifier").value(-0.1));
    }

    @Test
    void testMultipleConcurrentRequests() throws Exception {
        // Given
        List<CategoryInfo> ageCategories = Arrays.asList(
            createCategoryInfo("ADULT", "Adulte", "31-45 ans", 0.0)
        );
        List<CategoryInfo> profCategories = Arrays.asList(
            createCategoryInfo("EMPLOYEE", "Salarié CDI", "Employé en CDI", 0.0)
        );

        when(interestRateService.getAgeCategories()).thenReturn(ageCategories);
        when(interestRateService.getProfessionalCategories()).thenReturn(profCategories);

        // When & Then - Test that both endpoints work independently
        mockMvc.perform(get("/api/age-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ADULT"));

        mockMvc.perform(get("/api/professional-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("EMPLOYEE"));
    }
}