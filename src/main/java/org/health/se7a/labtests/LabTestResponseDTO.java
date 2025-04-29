package org.health.se7a.labtests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.health.se7a.nurse.NurseDTO;
import org.health.se7a.patients.PatientResponseDTO;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class LabTestResponseDTO {
    private Long id;
    private NurseDTO nurse;
    private String testName;
    private String result;
    private LocalDateTime testDate;
    private PatientResponseDTO patient;
}
