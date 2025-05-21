package com.demo.aerztekasse;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = AerztekasseApplication.class)
@AutoConfigureMockMvc
class AerztekasseApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test home endpoint")
    void homeEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Aerztekasse - code challenge - Home!"));
    }

    @Test
    @DisplayName("Test getAllPlaces endpoint")
    void getAllPlaces() throws Exception {
        mockMvc.perform(get("/places"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test getPlaceById endpoint")
    void getPlaceById() throws Exception {
        mockMvc.perform(get("/places/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

	@Test
    @DisplayName("Test deleteById endpoint")
    void deleteById() throws Exception {
        mockMvc.perform(delete("/places/2"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test createPlace endpoint")
    void createPlace() throws Exception {
        var json = StreamUtils.copyToString(
                        new ClassPathResource("places.json").getInputStream(),
                        StandardCharsets.UTF_8);

        mockMvc.perform(post("/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test createPlace endpoint - bad request validation")
    void createPlaceBadRequest() throws Exception {
        var json = StreamUtils.copyToString(
                        new ClassPathResource("places_bad_request.json").getInputStream(),
                        StandardCharsets.UTF_8);

        mockMvc.perform(post("/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test createPlace endpoint - bad request validation")
    void createPlaceMalformed() throws Exception {
        var json = StreamUtils.copyToString(
                        new ClassPathResource("places_malformed.json").getInputStream(),
                        StandardCharsets.UTF_8);

        mockMvc.perform(post("/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @ParameterizedTest
    @DisplayName("Test createPlace endpoint - bad request validation for wrong time")
    @CsvSource({"places_wrong_time_1.json,places_wrong_time_2.json,places_wrong_time_3.json"})
    void createPlaceWrongTime(String fileName) throws Exception {
        var json = StreamUtils.copyToString(
                        new ClassPathResource(fileName).getInputStream(),
                        StandardCharsets.UTF_8);

        mockMvc.perform(post("/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test groupedOpeningHoursStructure endpoint")
    void groupedOpeningHoursStructure() throws Exception {
        var response = mockMvc.perform(get("/places/1/opening-hours/grouped")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").isNotEmpty())
                .andExpect(jsonPath("$.location").isNotEmpty())
                .andExpect(jsonPath("$.openingHours").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var json = objectMapper.readTree(response);
        var openingHours = json.get("openingHours");

        for (JsonNode group : openingHours) {
            assertThat(group.get("day").isTextual()).isTrue();

            var hours = group.get("intervals");
            assertThat(hours).isNotNull();
            assertThat(hours.isArray() || hours.isTextual()).isTrue();
        }
    }
}