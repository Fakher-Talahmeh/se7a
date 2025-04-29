package org.health.se7a.assistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssistanceService {
    Boolean CreateAssistance(MedicalAssistanceDTO medicalAssistanceDTO);
    Boolean UpdateAssistance(MedicalAssistanceDTO medicalAssistanceDTO);
    Boolean DeleteAssistance(Long id);
    MedicalAssistanceResponseDTO GetMedicalAssistance(Long id);
    Page<MedicalAssistanceResponseDTO> GetAllMedicalAssistance(Pageable pageable);
    Page<MedicalAssistanceResponseDTO> GetMedicalAssistanceByNurse(Pageable pageable, Long nurseId);
    Page<MedicalAssistanceResponseDTO> GetMedicalAssistanceByDoctor(Pageable pageable, Long doctorId);
    Page<MedicalAssistanceResponseDTO> GetMedicalAssistanceByPatient(Pageable pageable, String patientId);
}
