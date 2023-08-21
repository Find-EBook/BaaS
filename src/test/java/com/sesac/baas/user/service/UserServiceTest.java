package com.sesac.baas.user.service;

import com.sesac.baas.user.entity.User;
import com.sesac.baas.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 테스트 시작 전에 사용자 저장
        testUser = User.builder()
                .email("testUser")
                .password("testPass")
                .build();
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        // 테스트 이후 사용자 삭제
        userRepository.delete(testUser);
    }
    @Test
    public void whenValidUser_thenShouldReturnTrue(){
        assertTrue(userService.login("testUser", "testPass"));
    }

    @Test
    public void whenInvalidPassword_thenShouldReturnFalse() {
        assertFalse(userService.login("testUser", "wrongPass"));
    }
}