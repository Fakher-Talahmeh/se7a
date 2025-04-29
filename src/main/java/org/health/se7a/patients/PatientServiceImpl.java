package org.health.se7a.patients;

import lombok.RequiredArgsConstructor;
import org.health.se7a.exception.XppException;
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
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public Boolean createPatient(PatientRequestDTO patientRequestDTO) {
        Patients patient = PatientMapper.toEntity(patientRequestDTO);
        patient.setCreatedAt(LocalDateTime.now());
        patientRepository.save(patient);
        return true;
    }

    @Override
    @Transactional
    public Boolean updatePatient(String natId, PatientRequestDTO patientRequestDTO) {
        Patients patient = patientRepository.getPatientsByNationalityID(natId)
                .orElseThrow(() -> notFoundException(natId, "patient.not.found"));

        updatePatientDetails(patient, patientRequestDTO);

        return true;
    }

    @Override
    public Boolean deletePatient(Long id) {
        Patients patient = patientRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "patient.not.found"));
        patientRepository.delete(patient);
        return true;
    }

    @Override
    public PatientResponseDTO getPatient(Long id) {
        return patientRepository.findById(id)
                .map(PatientMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "patient.not.found"));
    }

    @Override
    public Page<PatientResponseDTO> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(PatientMapper::toDto);
    }

    @Override
    public PatientResponseDTO getPatient(String id) {
        return patientRepository.getPatientsByNationalityID(id)
                .map(PatientMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "patient.not.found"));
    }
    @Override
    public Patients getPatientByNatId(String id) {
        return patientRepository.getPatientsByNationalityID(id)
                .orElseThrow(() -> notFoundException(id, "patient.not.found"));
    }



    private XppException notFoundException(Object identifier, String messageKey) {
        return new XppException(
                List.of(identifier),
                HttpStatus.NOT_FOUND,
                messageKey
        );
    }


    private void updatePatientDetails(Patients patient, PatientRequestDTO patientRequestDTO) {
        Optional.ofNullable(patientRequestDTO.getNationalityID()).ifPresent(patient::setNationalityID);
        Optional.ofNullable(patientRequestDTO.getAge()).ifPresent(patient::setAge);
        Optional.ofNullable(patientRequestDTO.getGender()).ifPresent(patient::setGender);
        patientRepository.save(patient);
    }

}
