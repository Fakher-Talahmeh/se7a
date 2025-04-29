package org.health.se7a.visits;

import lombok.RequiredArgsConstructor;
import org.health.se7a.doctor.DoctorMapper;
import org.health.se7a.exception.XppException;
import org.health.se7a.patients.PatientService;
import org.health.se7a.patients.Patients;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.doctor.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalVisitServiceImpl implements MedicalVisitService {

    private final MedicalVisitRepository medicalVisitRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Override
    public Page<MedicalVisitResponseDTO> getMedicalVisits(String natId, Pageable pageable) {
        return medicalVisitRepository
                .getMedicalVisitByPatients_NationalityID(natId, pageable)
                .map(MedicalVisitMapper::toResponse);
    }

    @Override
    public MedicalVisitResponseDTO getMedicalVisit(Long id) {
        MedicalVisit visit = medicalVisitRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "medical.visit.not.found"));
        return MedicalVisitMapper.toResponse(visit);
    }

    @Override
    public Long createMedicalVisit(MedicalVisitRequestDTO dto) {
        Patients patient = patientService.getPatientByNatId(dto.getPatientNatId());
        Doctor doctor = DoctorMapper.toEntity(doctorService.getDoctorById(dto.getDoctorId()));
        MedicalVisit visit = MedicalVisitMapper.toEntity(dto, doctor, patient);
        visit.setVisitDate(LocalDateTime.parse(dto.getVisitDate()));
        visit.setVisitTime(LocalDateTime.now());
        medicalVisitRepository.save(visit);
        return visit.getId();
    }

    @Override
    @Transactional
    public Boolean updateMedicalVisit(MedicalVisitRequestDTO dto) {
        MedicalVisit existing = medicalVisitRepository.findById(dto.getId())
                .orElseThrow(() -> notFoundException(dto.getId(), "medical.visit.not.found"));
        Optional.ofNullable(dto.getVisitReason()).ifPresent(existing::setVisitReason);
        Optional.ofNullable(dto.getNotes()).ifPresent(existing::setNotes);
        Optional.ofNullable(dto.getVisitDate())
                .ifPresent(date -> existing.setVisitDate(LocalDateTime.parse(date)));
        if (dto.getDoctorId() != null) {
            Doctor doctor = DoctorMapper.toEntity(doctorService.getDoctorById(dto.getDoctorId()));
            existing.setAttendingPhysician(doctor);
        }
        medicalVisitRepository.save(existing);
        return true;
    }

    @Override
    public Boolean deleteMedicalVisit(Long id) {
        MedicalVisit visit = medicalVisitRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "medical.visit.not.found"));
        medicalVisitRepository.delete(visit);
        return true;
    }

    private XppException notFoundException(Object identifier, String messageKey) {
        return new XppException(List.of(identifier), HttpStatus.NOT_FOUND, messageKey);
    }

    @Override
    public MedicalVisit getMedicalVisitById(Long id) {
        return medicalVisitRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "medical.visit.not.found"));
    }
}
