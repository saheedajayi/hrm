package com.devodata.hrm.services;

import com.devodata.hrm.data.models.Role;
import com.devodata.hrm.data.models.UserEntity;
import com.devodata.hrm.data.repository.UserRepository;
import com.devodata.hrm.dtos.AuthRequest;
import com.devodata.hrm.dtos.AuthResponse;
import com.devodata.hrm.exceptions.UserAlreadyExistException;
import com.devodata.hrm.security.JWTGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;
    @Override
    public String signUp(AuthRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            throw new UserAlreadyExistException("This username is already taken");
        }
        UserEntity user = UserEntity.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .roles(signUpRequest.getRoles())
                .build();
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public AuthResponse login(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new AuthResponse(token);
    }

    @PostConstruct
    public void createSuperAdmin() {
        UserEntity superAdmin = UserEntity.builder()
                .username("SuperAdmin")
                .password(passwordEncoder.encode("password"))
                .roles(Collections.singleton(Role.SUPER_ADMIN))
                .build();
        if (doesAdminExist()) {
            throw new UserAlreadyExistException("There cannot be more than one super admin logged in");
        }
        userRepository.save(superAdmin);
    }
    @PreDestroy
    public void deleteSuperAdmin() {
        Optional<UserEntity> retrievedUser = userRepository.findByUsername("SuperAdmin");
        retrievedUser.ifPresent(user -> {
            userRepository.delete(user);
        });

    }
    private boolean doesAdminExist() {
        return userRepository.findAll().stream()
                .map(user -> isExist(user.getRoles())).isParallel();
    }
    private boolean isExist(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.compareTo(Role.SUPER_ADMIN))
                .isParallel();

    }
}
