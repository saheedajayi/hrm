package com.devodata.hrm.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UpdateHealthRecordRequest {
    private Long id;
    private Date appointmentDate;
    private String vitalSigns;
    private String medications;
}
