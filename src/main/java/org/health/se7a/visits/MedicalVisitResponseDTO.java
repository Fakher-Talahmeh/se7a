package org.health.se7a.visits;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.health.se7a.diagnosis.Diagnosis;
import org.health.se7a.doctor.DoctorDTO;
import org.health.se7a.document.MedicalDocumentResponseDTO;
import org.health.se7a.labtests.LabTest;
import org.health.se7a.labtests.LabTestResponseDTO;
import org.health.se7a.medications.MedicationDTO;
import org.health.se7a.medications.MedicationResponseDTO;
import org.health.se7a.patients.PatientResponseDTO;
import org.health.se7a.vitalsigns.VitalSignsDTO;
import org.health.se7a.vitalsigns.VitalSignsResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalVisitResponseDTO {

    private Long id;
    private String visitReason;
    private String notes;
    private LocalDateTime visitDate;
    private DoctorDTO attendingPhysician;
    private List<VitalSignsResponseDTO> vitalSigns;
    private Diagnosis diagnosis;
    private List<MedicationResponseDTO> medications;

    private List<LabTestResponseDTO> labTests;

    private PatientResponseDTO patient;
    private List<MedicalDocumentResponseDTO> documents;
}
