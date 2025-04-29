package org.health.se7a.patients;

import org.health.se7a.medications.MedicationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {

    Boolean createPatient(PatientRequestDTO patientRequestDTO);
    Boolean updatePatient(String natId, PatientRequestDTO patientRequestDTO);
    Boolean deletePatient(Long id);
    PatientResponseDTO getPatient(Long id);
    Page<PatientResponseDTO> getAllPatients(Pageable pageable);
    PatientResponseDTO getPatient(String id);
    Patients getPatientByNatId(String natId);

}
