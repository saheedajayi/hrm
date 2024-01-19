package com.devodata.hrm.controllers;

import com.devodata.hrm.controllers.UserController;
import com.devodata.hrm.dtos.UpdateUserRequest;
import com.devodata.hrm.dtos.UserResponse;
import com.devodata.hrm.exceptions.UserNotFoundException;
import com.devodata.hrm.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUser_Success() {
        String username = "john_doe";
        UserResponse userResponse = new UserResponse();

        when(userService.getUser(username)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUser(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());

        verify(userService, times(1)).getUser(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUser_NotFound() {
        String username = "non_existing_user";

        when(userService.getUser(username)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserResponse> response = userController.getUser(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService, times(1)).getUser(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUser_InternalServerError() {
        String username = "john_doe";

        when(userService.getUser(username)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<UserResponse> response = userController.getUser(username);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(userService, times(1)).getUser(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void updateUser_Success() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        when(userService.updateUser(updateUserRequest)).thenReturn("User updated successfully");

        ResponseEntity<String> response = userController.updateUser(updateUserRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated successfully", response.getBody());

        verify(userService, times(1)).updateUser(updateUserRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void updateUser_Failure() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        when(userService.updateUser(updateUserRequest)).thenThrow(new RuntimeException("Update User failed"));

        ResponseEntity<String> response = userController.updateUser(updateUserRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Update User failed", response.getBody());

        verify(userService, times(1)).updateUser(updateUserRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUser_Success() {
        String username = "john_doe";

        when(userService.deleteUser(username)).thenReturn("User deleted successfully");

        ResponseEntity<String> response = userController.deleteUser(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());

        verify(userService, times(1)).deleteUser(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUser_NotFound() {
        String username = "non_existing_user";

        when(userService.deleteUser(username)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<String> response = userController.deleteUser(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService, times(1)).deleteUser(username);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUser_InternalServerError() {
        String username = "john_doe";

        when(userService.deleteUser(username)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<String> response = userController.deleteUser(username);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(userService, times(1)).deleteUser(username);
        verifyNoMoreInteractions(userService);
    }
}
