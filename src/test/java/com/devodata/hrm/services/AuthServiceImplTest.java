package com.devodata.hrm.services;

import com.devodata.hrm.data.models.Role;
import com.devodata.hrm.data.models.UserEntity;
import com.devodata.hrm.data.repository.UserRepository;
import com.devodata.hrm.dtos.AuthRequest;
import com.devodata.hrm.dtos.AuthResponse;
import com.devodata.hrm.exceptions.UserAlreadyExistException;
import com.devodata.hrm.security.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTGenerator jwtGenerator;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_Success() {
        AuthRequest signUpRequest = new AuthRequest();
        signUpRequest.setUsername("john_doe");
        signUpRequest.setPassword("password");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");
        signUpRequest.setRoles(Collections.singleton(Role.PATIENT));

        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");

        String response = authService.signUp(signUpRequest);

        assertEquals("User registered successfully", response);

        verify(userRepository, times(1)).existsByUsername("john_doe");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
    }

    @Test
    void signUp_UserAlreadyExists() {
        AuthRequest signUpRequest = new AuthRequest();
        signUpRequest.setUsername("john_doe");
        signUpRequest.setPassword("password");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");
        signUpRequest.setRoles(Collections.singleton(Role.PATIENT));

        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> authService.signUp(signUpRequest));

        verify(userRepository, times(1)).existsByUsername("john_doe");
        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
    }

    @Test
    void login_Success() {
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("john_doe");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn("token");

        AuthResponse authResponse = authService.login(loginRequest);

        assertNotNull(authResponse);
        assertEquals("token", authResponse.getAccessToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtGenerator, times(1)).generateToken(authentication);
        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
    }

//    @Test
//    void createSuperAdmin_Success() {
//        UserEntity superAdmin = UserEntity.builder()
//                .username("SuperAdmin")
//                .password("encoded_password") // use the actual encoded password here
//                .roles(Collections.singleton(Role.SUPER_ADMIN))
//                .build();
//
//        when(userRepository.findByUsername("SuperAdmin")).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
//
//        authService.createSuperAdmin();
//
//        verify(userRepository, times(1)).findByUsername("SuperAdmin");
//        verify(passwordEncoder, times(1)).encode("password");
//        verify(userRepository, times(1)).save(superAdmin);
//        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
//    }

    @Test
    void createSuperAdmin_Success() {
        UserEntity superAdmin = UserEntity.builder()
                .username("SuperAdmin")
                .password("encoded_password") // use the actual encoded password here
                .roles(Collections.singleton(Role.SUPER_ADMIN))
                .build();

        when(userRepository.findByUsername("SuperAdmin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");

        authService.createSuperAdmin();

        // Verify that findByUsername is invoked
        verify(userRepository, times(1)).findByUsername("SuperAdmin");

        // Verify that doesAdminExist is not invoked
        verify(userRepository, times(0)).findAll();

        // Verify other interactions
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(superAdmin);

        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
    }


    @Test
    void createSuperAdmin_SuperAdminAlreadyExists() {
        UserEntity existingSuperAdmin = UserEntity.builder()
                .username("SuperAdmin")
                .password("encoded_password") // use the actual encoded password here
                .roles(Collections.singleton(Role.SUPER_ADMIN))
                .build();

        when(userRepository.findByUsername("SuperAdmin")).thenReturn(Optional.of(existingSuperAdmin));

        assertThrows(UserAlreadyExistException.class, () -> authService.createSuperAdmin());

        verify(userRepository, times(1)).findByUsername("SuperAdmin");
        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
    }

    @Test
    void deleteSuperAdmin_Success() {
        UserEntity existingSuperAdmin = UserEntity.builder()
                .username("SuperAdmin")
                .password("password")
                .roles(Collections.singleton(Role.SUPER_ADMIN))
                .build();

        when(userRepository.findByUsername("SuperAdmin")).thenReturn(Optional.of(existingSuperAdmin));

        authService.deleteSuperAdmin();

        verify(userRepository, times(1)).findByUsername("SuperAdmin");
        verify(userRepository, times(1)).delete(existingSuperAdmin);
        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
    }

    @Test
    void deleteSuperAdmin_SuperAdminNotFound() {
        when(userRepository.findByUsername("SuperAdmin")).thenReturn(Optional.empty());

        authService.deleteSuperAdmin();

        verify(userRepository, times(1)).findByUsername("SuperAdmin");
        verifyNoMoreInteractions(userRepository, passwordEncoder, authenticationManager, jwtGenerator);
    }
}
