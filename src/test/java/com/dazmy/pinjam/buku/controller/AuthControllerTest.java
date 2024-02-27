package com.dazmy.pinjam.buku.controller;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.model.request.LoginRequest;
import com.dazmy.pinjam.buku.model.request.RegisterRequest;
import com.dazmy.pinjam.buku.model.response.CoreResponse;
import com.dazmy.pinjam.buku.model.response.LoginResponse;
import com.dazmy.pinjam.buku.repository.CredentialRepository;
import com.dazmy.pinjam.buku.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CredentialRepository credentialRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        credentialRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Adam Fadhilah Zamzam")
                .username("adam")
                .password("zamzam")
                .role("ROLE_USER")
                .dob("2003-12-26")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CoreResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals("OK", response.getData());
        });
    }

    @Test
    void testLoginSuccess() throws Exception {
        // register dlu ;v
        {
            RegisterRequest request = RegisterRequest.builder()
                    .name("Adam Fadhilah Zamzam")
                    .username("adam")
                    .password("zamzam")
                    .role("ROLE_USER")
                    .dob("2003-12-26")
                    .build();

            mockMvc.perform(
                    post("/api/v1/auth/register")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            ).andExpectAll(
                    status().isCreated()
            ).andDo(result -> {
                CoreResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

                assertEquals("OK", response.getData());
            });
        }

        // login
        LoginRequest loginRequest = LoginRequest.builder()
                .username("adam")
                .password("zamzam")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
           CoreResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
           });

           assertNotNull(response.getData().getAuthorization());
           assertEquals("ROLE_USER", response.getData().getRole());
        });
    }
}