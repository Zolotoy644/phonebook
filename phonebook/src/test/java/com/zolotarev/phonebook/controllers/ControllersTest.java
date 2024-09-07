package com.zolotarev.phonebook.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zolotarev.phonebook.dto.AuthRequest;
import com.zolotarev.phonebook.dto.AuthResponse;
import com.zolotarev.phonebook.dto.UserRequest;
import com.zolotarev.phonebook.entities.User;
import com.zolotarev.phonebook.repository.UserRepository;
import com.zolotarev.phonebook.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ControllersTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Test
    void registerUser() throws Exception {
        UserRequest userRequest = new UserRequest("sergey", "111111", User.Role.USER);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
        Optional<User> user = userRepository.findByUsername(userRequest.getUsername());
        assertTrue(user.isPresent());
    }

    @Test
    void authUser() throws Exception {

        AuthRequest authRequest = new AuthRequest("ivan", "111111");
        String responseString = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AuthResponse response = objectMapper.readValue(responseString, AuthResponse.class);

        assertEquals(authRequest.getUsername(), jwtService.extractUsername(response.getToken()));
    }
}
