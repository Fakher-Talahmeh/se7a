package org.health.se7a.diagnosis;

import lombok.*;
import org.health.se7a.doctor.DoctorDTO;
import org.health.se7a.visits.MedicalVisitResponseDTO;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisResponseDTO {
    private Long id;
    private DoctorDTO doctor;
    private String diagnosisDetails;
    private String notes;
    private LocalDateTime recordedAt;
    private MedicalVisitResponseDTO visit;
}
