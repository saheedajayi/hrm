package com.devodata.hrm.services;

import com.devodata.hrm.dtos.AuthRequest;
import com.devodata.hrm.dtos.AuthResponse;

public interface AuthService {
    String signUp(AuthRequest signUpRequest);

    AuthResponse login(AuthRequest loginRequest);
}
