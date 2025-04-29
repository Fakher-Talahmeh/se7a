package org.health.se7a.visits;

import org.health.se7a.doctor.Doctor;
import org.health.se7a.doctor.DoctorMapper;
import org.health.se7a.document.DocumentMapper;
import org.health.se7a.labtests.LabTestMapper;
import org.health.se7a.medications.MedicationMapper;
import org.health.se7a.patients.PatientMapper;
import org.health.se7a.patients.Patients;
import org.health.se7a.vitalsigns.VitalSignsMapper;

import java.time.LocalDateTime;
import java.util.Optional;

public class MedicalVisitMapper {

    public static MedicalVisitResponseDTO toResponse(MedicalVisit visit) {
        return Optional.ofNullable(visit)
                .map(v -> MedicalVisitResponseDTO.builder()
                        .id(v.getId())
                        .visitDate(v.getVisitDate())
                        .visitReason(v.getVisitReason())
                        .notes(v.getNotes())
                        .attendingPhysician(DoctorMapper.toDto(v.getAttendingPhysician()))
                        .vitalSigns(VitalSignsMapper.toDtoList(v.getVitalSigns()))
                        .diagnosis(v.getDiagnosis())
                        .patient(PatientMapper.toDto(visit.getPatients()))
                        .medications(MedicationMapper.toDtoList(v.getMedications()))
                        .labTests(LabTestMapper.toDtoList(v.getLabTests()))
                        .documents(DocumentMapper.toDtoList(v.getDocuments()))
                        .build())
                .orElse(null);
    }

    public static MedicalVisit toEntity(MedicalVisitRequestDTO dto, Doctor doctor, Patients patient) {
        return Optional.ofNullable(dto)
                .map(req -> MedicalVisit.builder()
                        .visitDate(LocalDateTime.parse(req.getVisitDate()))
                        .visitReason(req.getVisitReason())
                        .notes(req.getNotes())
                        .patients(patient)
                        .attendingPhysician(doctor)
                        .build())
                .orElse(null);
    }
}
