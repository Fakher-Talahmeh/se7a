package org.health.se7a.assistance;

import jakarta.validation.Valid;
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
@RequestMapping("/assistance")
@RequiredArgsConstructor
public class MedicalAssistanceController {

    private final AssistanceService assistanceService;

    @GetMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Page<MedicalAssistanceResponseDTO>> getAllAssistances(Pageable pageable) {
        return XppResponseEntity.map(assistanceService.GetAllMedicalAssistance(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrNurse()")
    public XppResponseEntity<MedicalAssistanceResponseDTO> getAssistanceById(@PathVariable Long id) {
        return XppResponseEntity.map(assistanceService.GetMedicalAssistance(id));
    }

    @GetMapping("/by-nurse/{nurseId}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrNurse()")
    public XppResponseEntity<Page<MedicalAssistanceResponseDTO>> getByNurse(Pageable pageable, @PathVariable Long nurseId) {
        return XppResponseEntity.map(assistanceService.GetMedicalAssistanceByNurse(pageable, nurseId));
    }

    @GetMapping("/by-doctor/{doctorId}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrNurse()")
    public XppResponseEntity<Page<MedicalAssistanceResponseDTO>> getByDoctor(Pageable pageable, @PathVariable Long doctorId) {
        return XppResponseEntity.map(assistanceService.GetMedicalAssistanceByDoctor(pageable, doctorId));
    }

    @GetMapping("/by-patient/{patientNatId}")
    @PreAuthorize("@authorizationService.loggedInUserIsAdminOrNurse()")
    public XppResponseEntity<Page<MedicalAssistanceResponseDTO>> getByPatient(Pageable pageable, @PathVariable String patientNatId) {
        return XppResponseEntity.map(assistanceService.GetMedicalAssistanceByPatient(pageable, patientNatId));
    }

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> createAssistance(@RequestBody @Validated(OnCreate.class) MedicalAssistanceDTO dto) {
        return XppResponseEntity.map(assistanceService.CreateAssistance(dto));
    }
    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> updateAssistance(@PathVariable Long id,
                                                       @RequestBody @Validated(OnUpdate.class) MedicalAssistanceDTO dto) {
        dto.setId(id);
        return XppResponseEntity.map(assistanceService.UpdateAssistance(dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> deleteAssistance(@PathVariable Long id) {
        return XppResponseEntity.map(assistanceService.DeleteAssistance(id));
    }
}
