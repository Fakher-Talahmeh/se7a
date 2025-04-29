package org.health.se7a.diagnosis;

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
@RequestMapping("/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @PostMapping()
    @PreAuthorize("@authorizationService.loggedInUserIsDoctor()")
    public XppResponseEntity<Boolean> createDiagnosis(
            @RequestParam Long visitId,
            @RequestBody @Validated(OnCreate.class) DiagnosisDTO dto) {
        Boolean created = diagnosisService.createDiagnosis(visitId, dto);
        return XppResponseEntity.map(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrDoctor()")
    public XppResponseEntity<Boolean> updateDiagnosis(
            @PathVariable Long id,
            @RequestBody @Validated(OnUpdate.class) DiagnosisDTO dto) {
        dto.setId(id);
        Boolean updated = diagnosisService.updateDiagnosis(dto);
        return XppResponseEntity.map(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrDoctor()")
    public XppResponseEntity<Boolean> deleteDiagnosis(@PathVariable Long id) {
        Boolean deleted = diagnosisService.deleteDiagnosis(id);
        return XppResponseEntity.map(deleted);
    }

    @GetMapping("/{id}")
    public XppResponseEntity<DiagnosisResponseDTO> getDiagnosisById(@PathVariable Long id) {
        return XppResponseEntity.map(diagnosisService.getDiagnosisById(id));
    }

    @GetMapping("/by-patient/{patientNatId}")
    public XppResponseEntity<Page<DiagnosisResponseDTO>> getDiagnosesByPatientId(
            @PathVariable String patientNatId,
            Pageable pageable) {
        return XppResponseEntity.map(diagnosisService.getDiagnosesByPatientId(patientNatId, pageable));
    }

    @GetMapping("/by-doctor/{doctorId}")
    @PreAuthorize("@authorizationService.userCanViewDoctorDetails(#doctorId)")
    public XppResponseEntity<Page<DiagnosisResponseDTO>> getDiagnosesByDoctorId(
            @PathVariable Long doctorId,
            Pageable pageable) {
        return XppResponseEntity.map(diagnosisService.getDiagnosisByDoctorId(doctorId, pageable));
    }
}
