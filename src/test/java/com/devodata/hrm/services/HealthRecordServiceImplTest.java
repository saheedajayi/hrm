package com.devodata.hrm.services;

import com.devodata.hrm.data.models.HealthRecord;
import com.devodata.hrm.data.models.UserEntity;
import com.devodata.hrm.data.repository.HealthRecordRepository;
import com.devodata.hrm.dtos.AddHealthRecordRequest;
import com.devodata.hrm.dtos.HealthRecordResponse;
import com.devodata.hrm.dtos.UpdateHealthRecordRequest;
import com.devodata.hrm.exceptions.HealthRecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthRecordServiceImplTest {

    @Mock
    private HealthRecordRepository healthRecordRepository;

    @InjectMocks
    private HealthRecordServiceImpl healthRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHealthRecord_Success() throws HealthRecordNotFoundException, ParseException {
        Long healthRecordId = 1L;

        // Parse the date string using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date appointmentDate = dateFormat.parse("2022-01-18");

        HealthRecord healthRecord = HealthRecord.builder()
                .id(healthRecordId)
                .appointmentDate(appointmentDate)
                .vitalSigns("Normal")
                .medications("Paracetamol")
                .user(new UserEntity())
                .build();

        when(healthRecordRepository.findById(healthRecordId)).thenReturn(Optional.of(healthRecord));

        HealthRecordResponse healthRecordResponse = healthRecordService.getHealthRecord(healthRecordId);

        assertNotNull(healthRecordResponse);
        assertEquals(healthRecordId, healthRecordResponse.getId());

        verify(healthRecordRepository, times(1)).findById(healthRecordId);
        verifyNoMoreInteractions(healthRecordRepository);
    }
    @Test
    void getHealthRecord_Success() throws HealthRecordNotFoundException, ParseException {
        Long healthRecordId = 1L;

        // Parse the date string using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date appointmentDate = dateFormat.parse("2022-01-18");

        HealthRecord healthRecord = HealthRecord.builder()
                .id(healthRecordId)
                .appointmentDate(appointmentDate)
                .vitalSigns("Normal")
                .medications("Paracetamol")
                .user(new UserEntity())
                .build();

        when(healthRecordRepository.findById(healthRecordId)).thenReturn(Optional.of(healthRecord));

        HealthRecordResponse healthRecordResponse = healthRecordService.getHealthRecord(healthRecordId);

        assertNotNull(healthRecordResponse);
        assertEquals(healthRecordId, healthRecordResponse.getId());

        verify(healthRecordRepository, times(1)).findById(healthRecordId);
        verifyNoMoreInteractions(healthRecordRepository);
    }

    @Test
    void getHealthRecord_HealthRecordNotFoundException() {
        Long healthRecordId = 1L;

        when(healthRecordRepository.findById(healthRecordId)).thenReturn(Optional.empty());

        assertThrows(HealthRecordNotFoundException.class, () -> healthRecordService.getHealthRecord(healthRecordId));

        verify(healthRecordRepository, times(1)).findById(healthRecordId);
        verifyNoMoreInteractions(healthRecordRepository);
    }

    @Test
    void updateHealthRecord_Success() {
        UpdateHealthRecordRequest updateHealthRecord = new UpdateHealthRecordRequest();
        updateHealthRecord.setId(1L);
        updateHealthRecord.setAppointmentDate(new Date("2022-01-19"));
        updateHealthRecord.setVitalSigns("Improved");
        updateHealthRecord.setMedications("Ibuprofen");

        HealthRecord existingHealthRecord = HealthRecord.builder()
                .id(updateHealthRecord.getId())
                .appointmentDate(new Date("2022-01-18"))
                .vitalSigns("Normal")
                .medications("Paracetamol")
                .user(new UserEntity())
                .build();

        when(healthRecordRepository.findById(updateHealthRecord.getId())).thenReturn(Optional.of(existingHealthRecord));
        when(healthRecordRepository.save(any(HealthRecord.class))).thenReturn(existingHealthRecord);

        String response = healthRecordService.updateHealthRecord(updateHealthRecord);

        assertEquals("Update Successful", response);
        assertEquals(updateHealthRecord.getAppointmentDate(), existingHealthRecord.getAppointmentDate());
        assertEquals(updateHealthRecord.getVitalSigns(), existingHealthRecord.getVitalSigns());
        assertEquals(updateHealthRecord.getMedications(), existingHealthRecord.getMedications());

        verify(healthRecordRepository, times(1)).findById(updateHealthRecord.getId());
        verify(healthRecordRepository, times(1)).save(any(HealthRecord.class));
        verifyNoMoreInteractions(healthRecordRepository);
    }

    @Test
    void deleteHealthRecord_Success() throws HealthRecordNotFoundException {
        Long healthRecordId = 1L;
        HealthRecord existingHealthRecord = HealthRecord.builder()
                .id(healthRecordId)
                .appointmentDate(new Date("2022-01-18"))
                .vitalSigns("Normal")
                .medications("Paracetamol")
                .user(new UserEntity())
                .build();

        when(healthRecordRepository.findById(healthRecordId)).thenReturn(Optional.of(existingHealthRecord));

        String response = healthRecordService.deleteHealthRecord(healthRecordId);

        assertEquals("Health Record with ID 1 deleted successfully", response);

        verify(healthRecordRepository, times(1)).findById(healthRecordId);
        verify(healthRecordRepository, times(1)).delete(existingHealthRecord);
        verifyNoMoreInteractions(healthRecordRepository);
    }

    @Test
    void deleteHealthRecord_HealthRecordNotFoundException() {
        Long healthRecordId = 1L;

        when(healthRecordRepository.findById(healthRecordId)).thenReturn(Optional.empty());

        assertThrows(HealthRecordNotFoundException.class, () -> healthRecordService.deleteHealthRecord(healthRecordId));

        verify(healthRecordRepository, times(1)).findById(healthRecordId);
        verifyNoMoreInteractions(healthRecordRepository);
    }
}
