package org.health.se7a.patients;

import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;
import org.health.se7a.common.XppResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Page<PatientResponseDTO>> getAllPatients(Pageable pageable) {
        return XppResponseEntity.map(patientService.getAllPatients(pageable));
    }

    @GetMapping("/{id}")
    public XppResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return XppResponseEntity.map(patientService.getPatient(id));
    }

    @GetMapping("/by-natId/{natId}")
    public XppResponseEntity<PatientResponseDTO> getPatientById(@PathVariable String natId) {
        return XppResponseEntity.map(patientService.getPatient(natId));
    }


    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsSecretary()")
    public XppResponseEntity<Boolean> createPatient(@RequestBody @Validated(OnCreate.class) PatientRequestDTO patientRequestDTO) {
        Boolean createdPatient = patientService.createPatient(patientRequestDTO);
        return XppResponseEntity.map(createdPatient);
    }


    @PutMapping("/{natId}")
    @PreAuthorize("@authorizationService.loggedInUserIsSecretary()")
    public XppResponseEntity<Boolean> updatePatient(
            @PathVariable String natId,
            @RequestBody @Validated(OnUpdate.class) PatientRequestDTO patientRequestDTO) {
        Boolean updatedPatient = patientService.updatePatient(natId, patientRequestDTO);
        return XppResponseEntity.map(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.userCanViewVitalSigns(#id)")
    public XppResponseEntity<Boolean> deletePatient(@PathVariable Long id) {
        Boolean deletedPatient = patientService.deletePatient(id);
        return XppResponseEntity.map(deletedPatient);
    }
}
