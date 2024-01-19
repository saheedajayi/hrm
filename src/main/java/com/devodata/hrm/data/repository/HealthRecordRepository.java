package com.devodata.hrm.data.repository;

import com.devodata.hrm.data.models.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
}
