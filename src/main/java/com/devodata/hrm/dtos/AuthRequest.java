package com.devodata.hrm.dtos;

import com.devodata.hrm.data.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String password;
    private Set<Role> roles;

}
