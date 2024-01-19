package com.devodata.hrm.services;

import com.devodata.hrm.data.models.Role;
import com.devodata.hrm.data.models.UserEntity;
import com.devodata.hrm.data.repository.UserRepository;
import com.devodata.hrm.dtos.UpdateUserRequest;
import com.devodata.hrm.dtos.UserResponse;
import com.devodata.hrm.exceptions.UserNotFoundException;
import com.devodata.hrm.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUser_Success() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .age(25)
                .roles(Collections.singleton(Role.PATIENT))
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        UserResponse userResponse = userService.getUser(username);

        // Assert
        assertEquals(userEntity.getId(), userResponse.getId());
        assertEquals(userEntity.getUsername(), userResponse.getUsername());
        assertEquals(userEntity.getFirstName(), userResponse.getFirstName());
        assertEquals(userEntity.getLastName(), userResponse.getLastName());
        assertEquals(userEntity.getAge(), userResponse.getAge());
        assertEquals("USER", userResponse.getRoles());

        // Verify
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void updateUser_Success() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .age(25)
                .roles(Collections.singleton(Role.PATIENT))
                .build();

        // Simulate creating an UpdateUserRequest instance without a constructor
        UpdateUserRequest updateUserRequest = createUpdateUserRequest(username, "UpdatedJohn", "UpdatedDoe", 30);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        String response = userService.updateUser(updateUserRequest);

        // Assert
        assertEquals("Update Successful", response);
        assertEquals("UpdatedJohn", userEntity.getFirstName());
        assertEquals("UpdatedDoe", userEntity.getLastName());
        assertEquals(30, userEntity.getAge());

        // Verify
        verify(userRepository, times(1)).findByUsername(username);
    }

    // Simulate creating an UpdateUserRequest instance without a constructor
    private UpdateUserRequest createUpdateUserRequest(String username, String firstName, String lastName, int age) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername(username);
        updateUserRequest.setFirstName(firstName);
        updateUserRequest.setLastName(lastName);
        updateUserRequest.setAge(age);
        return updateUserRequest;

    }
    @Test
    void deleteUser_Success() throws UserNotFoundException {
        // Arrange
        String username = "testUser";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .age(25)
                .roles(Collections.singleton(Role.PATIENT))
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        String response = userService.deleteUser(username);

        // Assert
        assertEquals("John Doe deleted successfully", response);

        // Verify
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).delete(userEntity);
    }
}
