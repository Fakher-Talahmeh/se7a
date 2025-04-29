package org.health.se7a.diagnosis;

import lombok.RequiredArgsConstructor;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.doctor.DoctorMapper;
import org.health.se7a.doctor.DoctorRepository;
import org.health.se7a.doctor.DoctorService;
import org.health.se7a.entity.EntityService;
import org.health.se7a.exception.XppException;
import org.health.se7a.patients.PatientService;
import org.health.se7a.patients.Patients;
import org.health.se7a.patients.PatientRepository;
import org.health.se7a.visits.MedicalVisit;
import org.health.se7a.visits.MedicalVisitRepository;
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
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Override
    @Transactional
    public Boolean createDiagnosis(Long id,DiagnosisDTO dto) {
        Patients patient = patientService.getPatientByNatId(dto.getPatientNatId());
        Doctor doctor = doctorService.getDoctor();
        if (!patient.getDoctors().contains(doctor)) {
            patient.getDoctors().add(doctor);
        }
        MedicalVisit visit = getOptionalVisit(id);
        Diagnosis diagnosis = DiagnosisMapper.toEntity(dto, patient, doctor, visit);
        diagnosis.setRecordedAt(LocalDateTime.now());
        diagnosisRepository.save(diagnosis);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateDiagnosis(DiagnosisDTO dto) {
        Diagnosis diagnosis = diagnosisRepository.findById(dto.getId())
                .orElseThrow(() -> notFoundException(dto.getId(), "diagnosis.not.found"));
        Optional.ofNullable(dto.getDiagnosisDetails()).ifPresent(diagnosis::setDiagnosisDetails);
        Optional.ofNullable(dto.getNotes()).ifPresent(diagnosis::setNotes);
        Optional.ofNullable(dto.getRecordedAt()).ifPresent(diagnosis::setRecordedAt);

        diagnosisRepository.save(diagnosis);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteDiagnosis(Long id) {
        if (!diagnosisRepository.existsById(id)) {
            throw notFoundException(id, "diagnosis.not.found");
        }
        Diagnosis diagnosis = diagnosisRepository
                .findById(id).orElseThrow(() -> notFoundException(id, "diagnosis.not.found"));
        diagnosisRepository.delete(diagnosis);
        return true;
    }

    @Override
    public DiagnosisResponseDTO getDiagnosisById(Long id) {
        return diagnosisRepository.findById(id)
                .map(DiagnosisMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "diagnosis.not.found"));
    }

    @Override
    public Page<DiagnosisResponseDTO> getDiagnosesByPatientId(String patientNatId, Pageable pageable) {
        return diagnosisRepository.findDiagnosesByPatient_NationalityID(patientNatId, pageable)
                .map(DiagnosisMapper::toDto);
    }

    @Override
    public Page<DiagnosisResponseDTO> getDiagnosisByDoctorId(Long doctorId, Pageable pageable) {
        return diagnosisRepository.findDiagnosisByDoctor_Id(doctorId, pageable)
                .map(DiagnosisMapper::toDto);
    }

    private MedicalVisit getOptionalVisit(Long visitId) {
        if (visitId == null) return null;
        return medicalVisitRepository.findById(visitId)
                .orElseThrow(() -> notFoundException(visitId, "visit.not.found"));
    }

    private XppException notFoundException(Object id, String messageKey) {
        return new XppException(List.of(id), HttpStatus.NOT_FOUND, messageKey);
    }
}
