package com.revio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revio.repository.SectionRepository;
import com.revio.repository.SubsectionRepository;
import com.revio.repository.TopicRepository;
import com.revio.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RevioApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubsectionRepository subsectionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri",
                () -> "mongodb://localhost:27017/revio_test_" + UUID.randomUUID());
        registry.add("jwt.secret",
                () -> "integration-test-secret-key-with-sufficient-length-1234567890-abcd-efgh");
    }

    @AfterEach
    void cleanUp() {
        topicRepository.deleteAll();
        subsectionRepository.deleteAll();
        sectionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void fullAuthAndSectionFlowWorks() throws Exception {
        String email = "integration+" + UUID.randomUUID() + "@example.com";

        String registerResponse = mockMvc.perform(post("/api/auth/register")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Integration Tester",
                                  "email": "%s",
                                  "password": "SecurePass123"
                                }
                                """.formatted(email)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = readJson(registerResponse).get("token").asText();

        String createSectionResponse = mockMvc.perform(post("/api/sections")
                        .contextPath("/api")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Web Development",
                                  "description": "Frontend and backend"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Web Development"))
                .andExpect(jsonPath("$.subsectionCount").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String sectionId = readJson(createSectionResponse).get("id").asText();
        assertThat(sectionRepository.findById(sectionId)).isPresent();

        mockMvc.perform(get("/api/sections")
                        .contextPath("/api")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sectionId));

        mockMvc.perform(get("/api/sections/{sectionId}", sectionId)
                        .contextPath("/api")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sectionId))
                .andExpect(jsonPath("$.title").value("Web Development"));

        mockMvc.perform(post("/api/sections/{sectionId}/subsections", sectionId)
                        .contextPath("/api")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Arrays",
                                  "description": "Core arrays"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Arrays"));
    }

    private JsonNode readJson(String content) throws Exception {
        return objectMapper.readTree(content);
    }
}
