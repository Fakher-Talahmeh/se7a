package org.health.se7a.vitalsigns;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VitalService {
    Boolean updateVitalSigns(VitalSignsDTO vitalSigns);
    Boolean deleteVitalSigns(Long id);
    Boolean createVitalSigns(Long visitId,VitalSignsDTO vitalSigns);
    Page<VitalSignsResponseDTO> getVitalSignsByPatient(String patientId, Pageable pageable);
    Page<VitalSignsResponseDTO> getVitalSignsDTOByNurse(Long nurseId, Pageable pageable);
    VitalSignsResponseDTO getVitalSignsDTOById(Long id);
}
