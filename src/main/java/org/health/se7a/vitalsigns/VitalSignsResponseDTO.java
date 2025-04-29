package org.health.se7a.vitalsigns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.health.se7a.nurse.NurseDTO;
import org.health.se7a.patients.PatientResponseDTO;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VitalSignsResponseDTO {

    private Long id;


    private NurseDTO nurse;

    private PatientResponseDTO patient;

    private Double bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Integer respiratoryRate;
    private LocalDateTime recordedAt;
}
