package com.devodata.hrm.controllers;

import com.devodata.hrm.controllers.HealthRecordController;
import com.devodata.hrm.dtos.AddHealthRecordRequest;
import com.devodata.hrm.dtos.HealthRecordResponse;
import com.devodata.hrm.dtos.UpdateHealthRecordRequest;
import com.devodata.hrm.exceptions.HealthRecordNotFoundException;
import com.devodata.hrm.services.HealthRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HealthRecordControllerTest {

    @Mock
    private HealthRecordService healthRecordService;

    @InjectMocks
    private HealthRecordController healthRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addHealthRecord_Success() {
        AddHealthRecordRequest newHealthRecord = new AddHealthRecordRequest();

        when(healthRecordService.addHealthRecord(newHealthRecord)).thenReturn("Health Record added successfully");

        ResponseEntity<String> response = healthRecordController.addHealthRecord(newHealthRecord);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Health Record added successfully", response.getBody());

        verify(healthRecordService, times(1)).addHealthRecord(newHealthRecord);
        verifyNoMoreInteractions(healthRecordService);
    }

    @Test
    void addHealthRecord_Failure() {
        AddHealthRecordRequest newHealthRecord = new AddHealthRecordRequest();

        when(healthRecordService.addHealthRecord(newHealthRecord)).thenThrow(new RuntimeException("Add Health Record failed"));

        ResponseEntity<String> response = healthRecordController.addHealthRecord(newHealthRecord);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Add Health Record failed", response.getBody());

        verify(healthRecordService, times(1)).addHealthRecord(newHealthRecord);
        verifyNoMoreInteractions(healthRecordService);
    }

    @Test
    void getHealthRecord_Success() throws HealthRecordNotFoundException {
        Long healthRecordId = 1L;
        HealthRecordResponse mockResponse = new HealthRecordResponse();

        when(healthRecordService.getHealthRecord(healthRecordId)).thenReturn(mockResponse);

        ResponseEntity<HealthRecordResponse> response = healthRecordController.getHealthRecord(healthRecordId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());

        verify(healthRecordService, times(1)).getHealthRecord(healthRecordId);
        verifyNoMoreInteractions(healthRecordService);
    }

    @Test
    void getHealthRecord_NotFound() throws HealthRecordNotFoundException {
        Long healthRecordId = 1L;

        when(healthRecordService.getHealthRecord(healthRecordId)).thenThrow(new HealthRecordNotFoundException("Health Record not found"));

        ResponseEntity<HealthRecordResponse> response = healthRecordController.getHealthRecord(healthRecordId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(healthRecordService, times(1)).getHealthRecord(healthRecordId);
        verifyNoMoreInteractions(healthRecordService);
    }

    @Test
    void updateHealthRecord_Success() {
        UpdateHealthRecordRequest updateHealthRecord = new UpdateHealthRecordRequest();

        when(healthRecordService.updateHealthRecord(updateHealthRecord)).thenReturn("Health Record updated successfully");

        ResponseEntity<String> response = healthRecordController.updateHealthRecord(updateHealthRecord);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Health Record updated successfully", response.getBody());

        verify(healthRecordService, times(1)).updateHealthRecord(updateHealthRecord);
        verifyNoMoreInteractions(healthRecordService);
    }

    @Test
    void updateHealthRecord_Failure() {
        UpdateHealthRecordRequest updateHealthRecord = new UpdateHealthRecordRequest();

        when(healthRecordService.updateHealthRecord(updateHealthRecord)).thenThrow(new RuntimeException("Update Health Record failed"));

        ResponseEntity<String> response = healthRecordController.updateHealthRecord(updateHealthRecord);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Update Health Record failed", response.getBody());

        verify(healthRecordService, times(1)).updateHealthRecord(updateHealthRecord);
        verifyNoMoreInteractions(healthRecordService);
    }

    @Test
    void deleteHealthRecord_Success() throws HealthRecordNotFoundException {
        Long healthRecordId = 1L;

        when(healthRecordService.deleteHealthRecord(healthRecordId)).thenReturn("Health Record deleted successfully");

        ResponseEntity<String> response = healthRecordController.deleteHealthRecord(healthRecordId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Health Record deleted successfully", response.getBody());

        verify(healthRecordService, times(1)).deleteHealthRecord(healthRecordId);
        verifyNoMoreInteractions(healthRecordService);
    }

    @Test
    void deleteHealthRecord_NotFound() throws HealthRecordNotFoundException {
        Long healthRecordId = 1L;

        when(healthRecordService.deleteHealthRecord(healthRecordId)).thenThrow(new HealthRecordNotFoundException("Health Record not found"));

        ResponseEntity<String> response = healthRecordController.deleteHealthRecord(healthRecordId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(healthRecordService, times(1)).deleteHealthRecord(healthRecordId);
        verifyNoMoreInteractions(healthRecordService);
    }
}

