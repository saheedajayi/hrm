package com.devodata.hrm.dtos;

import com.devodata.hrm.data.models.UserEntity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthRecordResponse {
    private Long id;
    private Date appointmentDate;
    private String vitalSigns;
    private String medications;
    private UserEntity user;
}
