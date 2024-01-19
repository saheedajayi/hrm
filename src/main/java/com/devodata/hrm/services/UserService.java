package com.devodata.hrm.services;

import com.devodata.hrm.dtos.UpdateUserRequest;
import com.devodata.hrm.dtos.UserResponse;

public interface UserService {

    UserResponse getUser(String username);
    String updateUser(UpdateUserRequest updateUser);
    String deleteUser(String username);
}
