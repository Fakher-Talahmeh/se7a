package org.health.se7a.assistance;

import lombok.*;
import org.health.se7a.nurse.NurseDTO;
import org.health.se7a.doctor.DoctorDTO;
import org.health.se7a.patients.PatientResponseDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalAssistanceResponseDTO {
    private Long id;
    private NurseDTO nurse;
    private DoctorDTO doctor;
    private PatientResponseDTO patient;
    private List<String> toolsPrepared;
    private String notes;
}
