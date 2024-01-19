package com.devodata.hrm.dtos;

import com.devodata.hrm.data.models.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String password;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
