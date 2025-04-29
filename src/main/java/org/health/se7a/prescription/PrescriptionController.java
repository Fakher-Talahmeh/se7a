package org.health.se7a.prescription;

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
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsDoctor()")
    public XppResponseEntity<Boolean> createPrescription(
            @RequestParam Long visitId,
            @RequestBody @Validated(OnCreate.class) PrescriptionRequestDTO dto) {
        Boolean created = prescriptionService.createPrescription(visitId, dto);
        return XppResponseEntity.map(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrDoctor()")
    public XppResponseEntity<Boolean> updatePrescription(
            @PathVariable Long id,
            @RequestBody @Validated(OnUpdate.class) PrescriptionRequestDTO dto) {
        dto.setId(id);
        Boolean updated = prescriptionService.updatePrescription(dto);
        return XppResponseEntity.map(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrDoctor()")
    public XppResponseEntity<Boolean> deletePrescription(@PathVariable Long id) {
        Boolean deleted = prescriptionService.deletePrescription(id);
        return XppResponseEntity.map(deleted);
    }

    @GetMapping("/{id}")
    public XppResponseEntity<PrescriptionResponseDTO> getPrescriptionById(@PathVariable Long id) {
        return XppResponseEntity.map(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/by-visit/{visitId}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrDoctor()")
    public XppResponseEntity<Page<PrescriptionResponseDTO>> getPrescriptionsByVisitId(
            @PathVariable Long visitId,
            Pageable pageable) {
        return XppResponseEntity.map(prescriptionService.getPrescriptionsByVisitId(visitId, pageable));
    }

    @GetMapping("/by-patient/{patientNatId}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrDoctor()")
    public XppResponseEntity<Page<PrescriptionResponseDTO>> getPrescriptionsByPatientId(
            @PathVariable String patientNatId,
            Pageable pageable) {
        return XppResponseEntity.map(prescriptionService.getPrescriptionByPatientId(patientNatId, pageable));
    }

    @GetMapping("/by-doctor/{doctorId}")
    @PreAuthorize("@authorizationService.userCanViewDoctorDetails(#doctorId)")
    public XppResponseEntity<Page<PrescriptionResponseDTO>> getPrescriptionsByDoctorId(
            @PathVariable Long doctorId,
            Pageable pageable) {
        return XppResponseEntity.map(prescriptionService.getPrescriptionByDoctorId(doctorId, pageable));
    }
}
