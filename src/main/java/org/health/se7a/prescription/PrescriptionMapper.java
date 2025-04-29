package org.health.se7a.prescription;

import org.health.se7a.doctor.DoctorMapper;
import org.health.se7a.patients.PatientMapper;
import org.health.se7a.visits.MedicalVisit;
import org.health.se7a.visits.MedicalVisitMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PrescriptionMapper {

    public static PrescriptionResponseDTO toDto(Prescription prescription) {
        return Optional.ofNullable(prescription)
                .map(p -> PrescriptionResponseDTO.builder()
                        .id(p.getId())
                        .medicationName(p.getMedicationName())
                        .dosage(p.getDosage())
                        .doctor(DoctorMapper.toDto(p.getDoctor()))
                        .patient(PatientMapper.toDto(p.getPatient()))
                        .duration(p.getDuration())
                        .instructions(p.getInstructions())
                        .visit(p.getVisit() != null ? MedicalVisitMapper.toResponse(p.getVisit()) : null)
                        .build())
                .orElse(null);
    }

    public static Prescription toEntity(PrescriptionRequestDTO dto, MedicalVisit visit) {
        return Optional.ofNullable(dto)
                .map(p -> Prescription.builder()
                        .medicationName(p.getMedicationName())
                        .dosage(p.getDosage())
                        .duration(p.getDuration())
                        .instructions(p.getInstructions())
                        .visit(visit)
                        .build())
                .orElse(null);
    }

    public static List<PrescriptionResponseDTO> toDtoList(List<Prescription> list) {
        return list == null ? null : list.stream()
                .map(PrescriptionMapper::toDto)
                .collect(Collectors.toList());
    }
}
