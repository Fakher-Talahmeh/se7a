package org.health.se7a.medications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.nurse.NurseDTO;
import org.health.se7a.patients.PatientResponseDTO;
import org.health.se7a.patients.Patients;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicationResponseDTO {
    private Long id;
    private String drugName;
    private Double dosage;
    private String administrationMethod;
    private LocalDateTime administeredAt;
    private NurseDTO nurse;
    private PatientResponseDTO patient;
}
