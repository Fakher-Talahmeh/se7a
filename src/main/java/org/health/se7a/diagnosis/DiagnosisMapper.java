package org.health.se7a.diagnosis;

import org.health.se7a.doctor.Doctor;
import org.health.se7a.doctor.DoctorMapper;
import org.health.se7a.patients.Patients;
import org.health.se7a.patients.PatientMapper;
import org.health.se7a.visits.MedicalVisit;
import org.health.se7a.visits.MedicalVisitMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiagnosisMapper {

    public static DiagnosisResponseDTO toDto(Diagnosis diagnosis) {
        return Optional.ofNullable(diagnosis)
                .map(d -> DiagnosisResponseDTO.builder()
                        .id(d.getId())
                        .doctor(DoctorMapper.toDto(d.getDoctor()))
                        .diagnosisDetails(d.getDiagnosisDetails())
                        .notes(d.getNotes())
                        .recordedAt(d.getRecordedAt())
                        .visit(d.getVisit() != null ? MedicalVisitMapper.toResponse(d.getVisit()) : null)
                        .build())
                .orElse(null);
    }

    public static Diagnosis toEntity(DiagnosisDTO dto, Patients patient, Doctor doctor, MedicalVisit visit) {
        return Optional.ofNullable(dto)
                .map(d -> Diagnosis.builder()
                        .patient(patient)
                        .doctor(doctor)
                        .diagnosisDetails(d.getDiagnosisDetails())
                        .notes(d.getNotes())
                        .recordedAt(d.getRecordedAt())
                        .visit(visit)
                        .build())
                .orElse(null);
    }

    public static List<DiagnosisResponseDTO> toDtoList(List<Diagnosis> list) {
        return list == null ? null : list.stream()
                .map(DiagnosisMapper::toDto)
                .collect(Collectors.toList());
    }
}
