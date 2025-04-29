package org.health.se7a.prescription;

import lombok.*;
import org.health.se7a.doctor.DoctorDTO;
import org.health.se7a.patients.PatientResponseDTO;
import org.health.se7a.patients.Patients;
import org.health.se7a.visits.MedicalVisitResponseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDTO {

    private Long id;
    private String medicationName;
    private String dosage;
    private String duration;
    private String instructions;
    private MedicalVisitResponseDTO visit;
    private DoctorDTO doctor;
    private PatientResponseDTO patient;
}
