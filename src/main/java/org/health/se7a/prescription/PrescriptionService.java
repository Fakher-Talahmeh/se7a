package org.health.se7a.prescription;

import org.health.se7a.diagnosis.DiagnosisResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionService {
    Boolean createPrescription(Long visitId, PrescriptionRequestDTO prescriptionDTO);
    Boolean updatePrescription(PrescriptionRequestDTO prescriptionDTO);
    Boolean deletePrescription(Long prescriptionId);
    PrescriptionResponseDTO getPrescriptionById(Long id);
    Page<PrescriptionResponseDTO> getPrescriptionsByVisitId(Long visitId, Pageable pageable);
    Page<PrescriptionResponseDTO> getPrescriptionByPatientId(String patientNatId, Pageable pageable);
    Page<PrescriptionResponseDTO> getPrescriptionByDoctorId(Long doctorId, Pageable pageable);
}
