package org.health.se7a.patients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.health.se7a.doctor.DoctorDTO;
import org.health.se7a.nurse.NurseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String nationalityID;
    private String name;
    private String telNumber;
    private LocalDateTime createdAt;
    private Long age;
    private Gender gender;
    private List<DoctorDTO> doctors;
    private List<NurseDTO> nurses;
    private List<ChronicDisease> chronicDiseases;
    private SmokingStatus smokingStatus;

}
