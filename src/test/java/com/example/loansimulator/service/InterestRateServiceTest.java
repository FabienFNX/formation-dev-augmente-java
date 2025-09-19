package com.example.loansimulator.service;

import com.example.loansimulator.dto.CategoryInfo;
import com.example.loansimulator.dto.InterestRateRequest;
import com.example.loansimulator.dto.InterestRateResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class InterestRateServiceTest {

    private InterestRateService interestRateService;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        interestRateService = new InterestRateService();
        ReflectionTestUtils.setField(interestRateService, "baseUrl",
            "http://localhost:" + mockWebServer.getPort());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testCalculateInterestRateSuccess() throws InterruptedException {
        // Given
        String mockResponse = "{"
            + "\"annualInterestRate\": 1.8,"
            + "\"baseRate\": 1.5,"
            + "\"ageModifier\": 0.0,"
            + "\"professionalModifier\": -0.2,"
            + "\"incomeModifier\": 0.5,"
            + "\"ageCategory\": \"ADULT\","
            + "\"professionalCategory\": \"EXECUTIVE\","
            + "\"monthlyNetIncome\": 4500.0"
            + "}";

        mockWebServer.enqueue(new MockResponse()
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"));

        // When
        double result = interestRateService.calculateInterestRate("ADULT", "EXECUTIVE", 4500.0);

        // Then
        assertEquals(1.8, result);

        RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/api/interest-rate/calculate", recordedRequest.getPath());
        String requestBody = recordedRequest.getBody().readUtf8();
        assertTrue(requestBody.contains("ADULT"));
        assertTrue(requestBody.contains("EXECUTIVE"));
        assertTrue(requestBody.contains("4500"));
    }

    @Test
    void testCalculateInterestRateWithError() {
        // Given
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(500)
            .setBody("Internal Server Error"));

        // When
        double result = interestRateService.calculateInterestRate("ADULT", "EXECUTIVE", 4500.0);

        // Then
        assertEquals(1.5, result); // Default rate
    }

    @Test
    void testCalculateInterestRateWithTimeout() {
        // Given
        mockWebServer.enqueue(new MockResponse()
            .setBody("{\"annualInterestRate\": 2.0}")
            .setBodyDelay(10, TimeUnit.SECONDS)); // Delay longer than timeout

        // When
        double result = interestRateService.calculateInterestRate("ADULT", "EXECUTIVE", 4500.0);

        // Then
        assertEquals(1.5, result); // Default rate due to timeout
    }

    @Test
    void testCalculateInterestRateWithNullResponse() {
        // Given
        mockWebServer.enqueue(new MockResponse()
            .setBody("null")
            .addHeader("Content-Type", "application/json"));

        // When
        double result = interestRateService.calculateInterestRate("ADULT", "EXECUTIVE", 4500.0);

        // Then
        assertEquals(1.5, result); // Default rate
    }

    @Test
    void testGetAgeCategoriesSuccess() throws InterruptedException {
        // Given
        String mockResponse = "["
            + "{"
                + "\"code\": \"YOUNG_ADULT\","
                + "\"name\": \"Jeune adulte\","
                + "\"description\": \"18-30 ans\","
                + "\"rateModifier\": 0.2"
            + "},"
            + "{"
                + "\"code\": \"ADULT\","
                + "\"name\": \"Adulte\","
                + "\"description\": \"31-45 ans\","
                + "\"rateModifier\": 0.0"
            + "}"
            + "]";

        mockWebServer.enqueue(new MockResponse()
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"));

        // When
        List<CategoryInfo> result = interestRateService.getAgeCategories();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("YOUNG_ADULT", result.get(0).getCode());
        assertEquals("Jeune adulte", result.get(0).getName());
        assertEquals(0.2, result.get(0).getRateModifier());

        RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/api/interest-rate/categories/age", recordedRequest.getPath());
    }

    @Test
    void testGetAgeCategoriesWithError() {
        // Given
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(404)
            .setBody("Not Found"));

        // When
        List<CategoryInfo> result = interestRateService.getAgeCategories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProfessionalCategoriesSuccess() throws InterruptedException {
        // Given
        String mockResponse = "["
            + "{"
                + "\"code\": \"EMPLOYEE\","
                + "\"name\": \"Salarié CDI\","
                + "\"description\": \"Employé en contrat à durée indéterminée\","
                + "\"rateModifier\": 0.0"
            + "},"
            + "{"
                + "\"code\": \"EXECUTIVE\","
                + "\"name\": \"Cadre\","
                + "\"description\": \"Cadre dirigeant ou ingénieur\","
                + "\"rateModifier\": -0.2"
            + "}"
            + "]";

        mockWebServer.enqueue(new MockResponse()
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"));

        // When
        List<CategoryInfo> result = interestRateService.getProfessionalCategories();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("EMPLOYEE", result.get(0).getCode());
        assertEquals("Salarié CDI", result.get(0).getName());
        assertEquals(0.0, result.get(0).getRateModifier());

        RecordedRequest recordedRequest = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/api/interest-rate/categories/professional", recordedRequest.getPath());
    }

    @Test
    void testGetProfessionalCategoriesWithTimeout() {
        // Given
        mockWebServer.enqueue(new MockResponse()
            .setBody("[]")
            .setBodyDelay(10, TimeUnit.SECONDS)); // Delay longer than timeout

        // When
        List<CategoryInfo> result = interestRateService.getProfessionalCategories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateInterestRateWithDifferentProfiles() {
        // Test various profile combinations
        String[] ageCategories = {"YOUNG_ADULT", "ADULT", "SENIOR"};
        String[] profCategories = {"EMPLOYEE", "EXECUTIVE", "FREELANCER"};
        double[] incomes = {2000.0, 4500.0, 8000.0};

        for (String age : ageCategories) {
            for (String prof : profCategories) {
                for (double income : incomes) {
                    // Given
                    double expectedRate = 1.5 + Math.random(); // Random rate for test
                    mockWebServer.enqueue(new MockResponse()
                        .setBody("{\"annualInterestRate\": " + expectedRate + "}")
                        .addHeader("Content-Type", "application/json"));

                    // When
                    double result = interestRateService.calculateInterestRate(age, prof, income);

                    // Then
                    assertEquals(expectedRate, result, 0.001);
                }
            }
        }
    }
}