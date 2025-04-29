package org.health.se7a.medications;

import lombok.RequiredArgsConstructor;
import org.health.se7a.exception.XppException;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.nurse.NurseService;
import org.health.se7a.patients.PatientService;
import org.health.se7a.patients.Patients;
import org.health.se7a.security.util.SecurityContextUtil;
import org.health.se7a.visits.MedicalVisitService;
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
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final NurseService nurseService;
    private final PatientService patientService;
    private final MedicalVisitService medicalVisitService;

    @Override
    public Boolean addMedication(Long visitId,MedicationDTO medicationDTO) {
        Patients patient = patientService.getPatientByNatId(medicationDTO.getPatientNatId());
        Nurse nurse = nurseService.getNurse();
        if (!patient.getNurses().contains(nurse)) {
            patient.getNurses().add(nurse);
        }
        Medication medication = MedicationMapper.toEntity(medicationDTO,patient,nurse);
        medication.setAdministeredAt(LocalDateTime.now());
        medication.setVisit(medicalVisitService.getMedicalVisitById(visitId));
        medicationRepository.save(medication);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateMedication(MedicationDTO medication) {
        Medication existingMedication = medicationRepository.findById(medication.getId())
                .orElseThrow(() -> notFoundException(medication.getId(), "medication.not.found"));
        updateMedicationDetails(existingMedication, medication);
        return true;
    }

    @Override
    public Boolean deleteMedication(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "medication.not.found"));
        medicationRepository.delete(medication);
        return true;
    }

    @Override
    public MedicationResponseDTO findMedicationById(Long id) {
        return medicationRepository.findById(id)
                .map(MedicationMapper::toResponse)
                .orElseThrow(() -> notFoundException(id, "medication.not.found"));
    }

    @Override
    public Page<MedicationResponseDTO> getMedicationByPatientNatId(String natId, Pageable pageable) {
        return medicationRepository.findByPatient_NationalityID(natId,pageable)
                .map(MedicationMapper::toResponse);
    }

    @Override
    public Page<MedicationResponseDTO> getMedicationByNurse(Pageable pageable) {
        return medicationRepository.findMedicationsByNurseId(loggedInUserId(), pageable)
                .map(MedicationMapper::toResponse);
    }



    private XppException notFoundException(Object identifier, String messageKey) {
        return new XppException(
                List.of(identifier),
                HttpStatus.NOT_FOUND,
                messageKey
        );
    }

    private void updateMedicationDetails(Medication existingMedication, MedicationDTO newMedication) {
        Optional.ofNullable(newMedication.getDrugName()).ifPresent(existingMedication::setDrugName);
        Optional.ofNullable(newMedication.getDosage()).ifPresent(existingMedication::setDosage);
        Optional.ofNullable(newMedication.getAdministrationMethod()).ifPresent(existingMedication::setAdministrationMethod);
        existingMedication.setAdministeredAt(LocalDateTime.now());
        medicationRepository.save(existingMedication);
    }
    private Long loggedInUserId() {
        return SecurityContextUtil.loggedUser().getId();
    }

}