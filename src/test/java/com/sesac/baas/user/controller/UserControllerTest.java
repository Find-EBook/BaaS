package com.sesac.baas.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.baas.user.dto.UserLoginRequest;
import com.sesac.baas.user.entity.User;
import com.sesac.baas.user.repository.UserRepository;
import com.sesac.baas.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension; // 수정된 import
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders; // 추가된 import

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // 추가된 import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// 수정된 어노테이션
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @MockBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 테스트 시작 전에 사용자 저장
        testUser = User.builder()
                .email("test@example.com")
                .password("password123")
                .build();
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        // 테스트 이후 사용자 삭제
        userRepository.delete(testUser);
    }
    @Test
    public void testLoginSuccess() throws Exception {
        String email = "test@example.com";
        String password = "password123";

        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsMock.getPassword()).thenReturn(password);

        when(userService.loadUserByUsername(email)).thenReturn(userDetailsMock);
        when(userService.checkPassword(password, password)).thenReturn(true);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new UserLoginRequest(email, password))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());  // Assuming the token exists in the response body
    }

    @Test
    public void testLoginFailure() throws Exception {
        String email = "test@example.com";
        String password = "wrongpassword";

        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsMock.getPassword()).thenReturn("password123");  // Setting to a different password

        when(userService.loadUserByUsername(email)).thenReturn(userDetailsMock);
        when(userService.checkPassword(password, "password123")).thenReturn(false);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new UserLoginRequest(email, password))))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

}
