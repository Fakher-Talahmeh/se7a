package org.health.se7a.labtests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LabTestService {
    Boolean createLabTest(Long visitId,LabTestDTO labTestDTO);
    Boolean updateLabTest(LabTestDTO labTestDTO);
    Boolean deleteLabTest(Long id);
    Page<LabTestResponseDTO> getLabTestsByPatientNatId(String natId, Pageable pageable);
    LabTestResponseDTO getLabTestById(Long id);
    Page<LabTestResponseDTO> getByNurse(Pageable pageable);
}
