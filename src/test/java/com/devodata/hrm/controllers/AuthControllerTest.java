package com.devodata.hrm.controllers;

import com.devodata.hrm.controllers.AuthController;
import com.devodata.hrm.dtos.AuthRequest;
import com.devodata.hrm.dtos.AuthResponse;
import com.devodata.hrm.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_Success() {
        AuthRequest signUpRequest = new AuthRequest();
        signUpRequest.setUsername("testUsername");
        signUpRequest.setPassword("testPassword");

        when(authService.signUp(signUpRequest)).thenReturn("User registered successfully");

        ResponseEntity<String> response = authController.signUp(signUpRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        verify(authService, times(1)).signUp(signUpRequest);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void signUp_Failure() {
        AuthRequest signUpRequest = new AuthRequest();
        signUpRequest.setUsername("testUsername");
        signUpRequest.setPassword("testPassword");

        when(authService.signUp(signUpRequest)).thenThrow(new RuntimeException("Registration failed"));

        ResponseEntity<String> response = authController.signUp(signUpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Registration failed", response.getBody());

        verify(authService, times(1)).signUp(signUpRequest);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void login_Success() {
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("testUsername");
        loginRequest.setPassword("testPassword");

        AuthResponse mockAuthResponse = new AuthResponse("mockToken");
        when(authService.login(loginRequest)).thenReturn(mockAuthResponse);

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAuthResponse, response.getBody());

        verify(authService, times(1)).login(loginRequest);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void login_Failure() {
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("testUsername");
        loginRequest.setPassword("testPassword");

        when(authService.login(loginRequest)).thenThrow(new RuntimeException("Login failed"));

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        verify(authService, times(1)).login(loginRequest);
        verifyNoMoreInteractions(authService);
    }
}
