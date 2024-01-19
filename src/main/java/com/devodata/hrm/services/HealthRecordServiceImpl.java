package com.devodata.hrm.services;

import com.devodata.hrm.data.models.HealthRecord;
import com.devodata.hrm.data.models.UserEntity;
import com.devodata.hrm.data.repository.HealthRecordRepository;
import com.devodata.hrm.dtos.AddHealthRecordRequest;
import com.devodata.hrm.dtos.HealthRecordResponse;
import com.devodata.hrm.dtos.UpdateHealthRecordRequest;
import com.devodata.hrm.exceptions.HealthRecordNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class HealthRecordServiceImpl implements HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;

    @Override
    public String addHealthRecord(AddHealthRecordRequest newHealthRecord) {
        HealthRecord healthRecord = HealthRecord.builder()
                .appointmentDate(newHealthRecord.getAppointmentDate())
                .vitalSigns(newHealthRecord.getVitalSigns())
                .medications(newHealthRecord.getMedications())
                .user(newHealthRecord.getUser())
                .build();

        HealthRecord savedRecord = healthRecordRepository.save(healthRecord);
        return String.format("Health Record with ID: %d added successfully", savedRecord.getId());
    }

    public HealthRecordResponse getHealthRecord(Long healthRecordId) throws HealthRecordNotFoundException {
        HealthRecord healthRecord = getHealthRecordById(healthRecordId);

        return new HealthRecordResponse(
                healthRecord.getId(),
                healthRecord.getAppointmentDate(),
                healthRecord.getVitalSigns(),
                healthRecord.getMedications(),
                healthRecord.getUser()
        );
    }

    @Override
    public String updateHealthRecord(UpdateHealthRecordRequest updateHealthRecord) {
        HealthRecord healthRecord = getHealthRecordById(updateHealthRecord.getId());

        updateIfNotNull(healthRecord::setAppointmentDate, updateHealthRecord.getAppointmentDate());
        updateIfNotNull(healthRecord::setVitalSigns, updateHealthRecord.getVitalSigns());
        updateIfNotNull(healthRecord::setMedications, updateHealthRecord.getMedications());

        return "Update Successful";
    }

    @Override
    public String deleteHealthRecord(Long healthRecordId) throws HealthRecordNotFoundException {
        HealthRecord healthRecord = getHealthRecordById(healthRecordId);
        healthRecordRepository.delete(healthRecord);
        return String.format("Health Record with ID %d deleted successfully", healthRecordId);
    }


    private HealthRecord getHealthRecordById(Long healthRecordId) throws HealthRecordNotFoundException {
        Optional<HealthRecord> optionalHealthRecord = healthRecordRepository.findById(healthRecordId);
        return optionalHealthRecord.orElseThrow(() -> new HealthRecordNotFoundException(String.format("Health Record not found with ID: %d", healthRecordId)));
    }

    private <T> void updateIfNotNull(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
