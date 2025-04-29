package org.health.se7a.assistance;

import org.health.se7a.doctor.DoctorMapper;
import org.health.se7a.nurse.NurseMapper;
import org.health.se7a.patients.PatientMapper;
import org.health.se7a.patients.Patients;

import java.util.Optional;

public class MedicalAssistanceMapper {

    public static MedicalAssistanceResponseDTO toResponse(MedicalAssistance assistance) {
        return Optional.ofNullable(assistance)
                .map(a -> MedicalAssistanceResponseDTO.builder()
                        .id(a.getId())
                        .nurse(NurseMapper.toDto(a.getNurse()))
                        .doctor(DoctorMapper.toDto(a.getDoctor()))
                        .patient(PatientMapper.toDto(a.getPatient()))
                        .toolsPrepared(a.getToolsPrepared())
                        .notes(a.getNotes())
                        .build())
                .orElse(null);
    }

    public static MedicalAssistance toEntity(MedicalAssistanceDTO dto, Patients patient) {
        return Optional.ofNullable(dto)
                .map(d -> MedicalAssistance.builder()
                        .patient(patient)
                        .notes(d.getNotes())
                        .toolsPrepared(d.getToolsPrepared())
                        .build())
                .orElse(null);
    }
}
