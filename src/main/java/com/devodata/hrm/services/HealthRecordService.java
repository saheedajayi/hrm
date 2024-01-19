package com.devodata.hrm.services;

import com.devodata.hrm.data.models.UserEntity;
import com.devodata.hrm.dtos.AddHealthRecordRequest;
import com.devodata.hrm.dtos.HealthRecordResponse;
import com.devodata.hrm.dtos.UpdateHealthRecordRequest;

public interface HealthRecordService {
    public String addHealthRecord(AddHealthRecordRequest newHealthRecord);
    HealthRecordResponse getHealthRecord(Long healthRecordId);
    public String updateHealthRecord(UpdateHealthRecordRequest updateHealthRecord);
    String deleteHealthRecord(Long healthRecordId);
}
