package org.health.se7a.diagnosis;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiagnosisService {
    Boolean createDiagnosis(Long id,DiagnosisDTO diagnosis);
    Boolean updateDiagnosis(DiagnosisDTO diagnosis);
    Boolean deleteDiagnosis(Long diagnosisId);
    DiagnosisResponseDTO getDiagnosisById(Long id);
    Page<DiagnosisResponseDTO> getDiagnosesByPatientId(String patientNatId, Pageable pageable);
    Page<DiagnosisResponseDTO> getDiagnosisByDoctorId(Long doctorId, Pageable pageable);
}
