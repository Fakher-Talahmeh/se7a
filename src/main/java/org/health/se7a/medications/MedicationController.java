package org.health.se7a.medications;

import lombok.RequiredArgsConstructor;
import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;
import org.health.se7a.common.XppResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping("/{id}")
    public XppResponseEntity<MedicationResponseDTO> getMedicationById(@PathVariable Long id) {
        MedicationResponseDTO medication = medicationService.findMedicationById(id);
        return XppResponseEntity.map(medication);
    }

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> addMedication(@RequestParam Long visitId,@RequestBody @Validated(OnCreate.class) MedicationDTO medicationDTO) {
        return XppResponseEntity.map(medicationService.addMedication(visitId,medicationDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> updateMedication(
            @PathVariable Long id,
            @RequestBody @Validated(OnUpdate.class) MedicationDTO medicationDTO) {
        medicationDTO.setId(id);
        return XppResponseEntity.map(medicationService.updateMedication(medicationDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> deleteMedication(@PathVariable Long id) {
        return XppResponseEntity.map(medicationService.deleteMedication(id));
    }

    @GetMapping("/by-patient/{patientNatId}")
    public XppResponseEntity<Page<MedicationResponseDTO>> getMedicationsByPatient(@PathVariable String patientNatId, Pageable pageable) {
        return XppResponseEntity.map(medicationService.getMedicationByPatientNatId(patientNatId, pageable));
    }

    @GetMapping("/by-nurse")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Page<MedicationResponseDTO>> getMedicationsByLoggedInNurse(Pageable pageable) {
        return XppResponseEntity.map(medicationService.getMedicationByNurse(pageable));
    }
}
