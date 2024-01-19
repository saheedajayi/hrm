package com.devodata.hrm.dtos;

import com.devodata.hrm.data.models.UserEntity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AddHealthRecordRequest {
    private Date appointmentDate;
    private String vitalSigns;
    private String medications;
    private UserEntity user;
}
