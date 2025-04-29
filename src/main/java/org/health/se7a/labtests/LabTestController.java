package org.health.se7a.labtests;

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
@RequestMapping("/labtests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestService labTestService;

    @GetMapping("/{id}")
    public XppResponseEntity<LabTestResponseDTO> getLabTestById(@PathVariable Long id) {
        return XppResponseEntity.map(labTestService.getLabTestById(id));
    }

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> createLabTest(@RequestParam Long visitId,@RequestBody @Validated(OnCreate.class) LabTestDTO labTestDTO) {
        return XppResponseEntity.map(labTestService.createLabTest(visitId,labTestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> updateLabTest(
            @PathVariable Long id,
            @RequestBody @Validated(OnUpdate.class) LabTestDTO labTestDTO) {
        labTestDTO.setId(id);
        return XppResponseEntity.map(labTestService.updateLabTest(labTestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Boolean> deleteLabTest(@PathVariable Long id) {
        return XppResponseEntity.map(labTestService.deleteLabTest(id));
    }

    @GetMapping("/by-patient/{patientNatId}")
    public XppResponseEntity<Page<LabTestResponseDTO>> getLabTestsByPatient(@PathVariable String patientNatId, Pageable pageable) {
        return XppResponseEntity.map(labTestService.getLabTestsByPatientNatId(patientNatId, pageable));
    }

    @GetMapping("/by-nurse")
    @PreAuthorize("@authorizationService.loggedInUserIsNurse()")
    public XppResponseEntity<Page<LabTestResponseDTO>> getLabTestsByLoggedInNurse(Pageable pageable) {
        return XppResponseEntity.map(labTestService.getByNurse(pageable));
    }
}
