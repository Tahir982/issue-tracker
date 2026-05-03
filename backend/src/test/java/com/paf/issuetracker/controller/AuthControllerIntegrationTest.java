package com.paf.issuetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paf.issuetracker.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void register_withValidData_returns201() throws Exception {
        var req = new RegisterRequest();
        req.setUsername("intuser_" + System.currentTimeMillis());
        req.setEmail("int" + System.currentTimeMillis() + "@test.com");
        req.setPassword("secure123");
        req.setFullName("Integration User");

        mockMvc.perform(post("/api/auth/register")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.data.token").exists())
               .andExpect(jsonPath("$.data.tokenType").value("Bearer"));
    }

    @Test
    void register_withBlankUsername_returns400() throws Exception {
        var req = new RegisterRequest();
        req.setUsername(""); req.setEmail("v@v.com");
        req.setPassword("pass123"); req.setFullName("Test");

        mockMvc.perform(post("/api/auth/register")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void login_withWrongCredentials_returns401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
               .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"nobody\",\"password\":\"wrong\"}"))
               .andExpect(status().isUnauthorized());
    }
}
