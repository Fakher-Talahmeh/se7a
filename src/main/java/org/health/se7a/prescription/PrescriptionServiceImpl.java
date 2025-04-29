package org.health.se7a.prescription;

import lombok.RequiredArgsConstructor;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.doctor.DoctorService;
import org.health.se7a.exception.XppException;
import org.health.se7a.patients.PatientService;
import org.health.se7a.patients.Patients;
import org.health.se7a.visits.MedicalVisit;
import org.health.se7a.visits.MedicalVisitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Override
    @Transactional
    public Boolean createPrescription(Long visitId, PrescriptionRequestDTO dto) {
        Patients patient = patientService.getPatientByNatId(dto.getPatientNatId());
        MedicalVisit visit = getOptionalVisit(visitId);
        Doctor doctor = doctorService.getDoctor();
        Prescription prescription = PrescriptionMapper.toEntity(dto, visit);
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescriptionRepository.save(prescription);
        return true;
    }

    @Override
    @Transactional
    public Boolean updatePrescription(PrescriptionRequestDTO dto) {
        Prescription prescription = prescriptionRepository.findById(dto.getId())
                .orElseThrow(() -> notFoundException(dto.getId(), "prescription.not.found"));

        Optional.ofNullable(dto.getMedicationName()).ifPresent(prescription::setMedicationName);
        Optional.ofNullable(dto.getDosage()).ifPresent(prescription::setDosage);
        Optional.ofNullable(dto.getDuration()).ifPresent(prescription::setDuration);
        Optional.ofNullable(dto.getInstructions()).ifPresent(prescription::setInstructions);

        prescriptionRepository.save(prescription);
        return true;
    }

    @Override
    @Transactional
    public Boolean deletePrescription(Long prescriptionId) {
        if (!prescriptionRepository.existsById(prescriptionId)) {
            throw notFoundException(prescriptionId, "prescription.not.found");
        }
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> notFoundException(prescriptionId, "prescription.not.found"));
        prescriptionRepository.delete(prescription);
        return true;
    }

    @Override
    public PrescriptionResponseDTO getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .map(PrescriptionMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "prescription.not.found"));
    }

    @Override
    public Page<PrescriptionResponseDTO> getPrescriptionsByVisitId(Long visitId, Pageable pageable) {
        return prescriptionRepository.findPrescriptionByVisit_Id(visitId, pageable)
                .map(PrescriptionMapper::toDto);
    }

    @Override
    public Page<PrescriptionResponseDTO> getPrescriptionByPatientId(String patientNatId, Pageable pageable) {
        return prescriptionRepository.findPrescriptionByPatient_NationalityID(patientNatId, pageable)
                .map(PrescriptionMapper::toDto);
    }

    @Override
    public Page<PrescriptionResponseDTO> getPrescriptionByDoctorId(Long doctorId, Pageable pageable) {
        return prescriptionRepository.findPrescriptionByDoctor_Id(doctorId, pageable)
                .map(PrescriptionMapper::toDto);
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
