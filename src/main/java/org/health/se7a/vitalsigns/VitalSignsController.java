package org.health.se7a.vitalsigns;


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
@RequestMapping("/vital-signs")
@RequiredArgsConstructor
public class VitalSignsController {

    private final VitalService vitalService;

    @GetMapping("/by-patient/{patientNatId}")
    public XppResponseEntity<Page<VitalSignsResponseDTO>> getVitalSignsByPatient(@PathVariable String patientNatId, Pageable pageable) {
        return XppResponseEntity.map(vitalService.getVitalSignsByPatient(patientNatId, pageable));
    }

    @GetMapping("/by-nurse/{nurseId}")
    @PreAuthorize("@authorizationService.userCanViewNurseDetails(#nurseId)")
    public XppResponseEntity<Page<VitalSignsResponseDTO>> getVitalSignsByNurse(@PathVariable Long nurseId, Pageable pageable) {
        return XppResponseEntity.map(vitalService.getVitalSignsDTOByNurse(nurseId, pageable));
    }

    @GetMapping("/{id}")
    public XppResponseEntity<VitalSignsResponseDTO> getVitalSignsById(@PathVariable Long id) {
        return XppResponseEntity.map(vitalService.getVitalSignsDTOById(id));
    }

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> createVitalSigns(@RequestBody @Validated(OnCreate.class)  VitalSignsDTO vitalSignsDTO,@RequestParam Long visitId) {
        return XppResponseEntity.map(vitalService.createVitalSigns(visitId,vitalSignsDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> updateVitalSigns(
            @PathVariable Long id,
            @RequestBody @Validated(OnUpdate.class) VitalSignsDTO vitalSignsDTO) {
        vitalSignsDTO.setId(id);
        return XppResponseEntity.map(vitalService.updateVitalSigns(vitalSignsDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> deleteVitalSigns(@PathVariable Long id) {
        return XppResponseEntity.map(vitalService.deleteVitalSigns(id));
    }
}
