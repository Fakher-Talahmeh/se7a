package org.health.se7a.visits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalVisitService {

    Page<MedicalVisitResponseDTO> getMedicalVisits(String natId,Pageable pageable);
    MedicalVisitResponseDTO getMedicalVisit(Long id);
    Long createMedicalVisit(MedicalVisitRequestDTO medicalVisit);
    Boolean updateMedicalVisit(MedicalVisitRequestDTO medicalVisit);
    Boolean deleteMedicalVisit(Long id);
    MedicalVisit getMedicalVisitById(Long id);

}
