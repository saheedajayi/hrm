package com.devodata.hrm.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date appointmentDate;
    private String vitalSigns;
    private String medications;
    @OneToOne
    private UserEntity user;
}
