package org.health.se7a.medications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicationService {
    Boolean addMedication(Long visitId,MedicationDTO medication);
    Boolean updateMedication(MedicationDTO medication);
    Boolean deleteMedication(Long id);
    MedicationResponseDTO findMedicationById(Long id);
    Page<MedicationResponseDTO> getMedicationByPatientNatId(String natId,Pageable pageable);
    Page<MedicationResponseDTO> getMedicationByNurse(Pageable pageable);


}
