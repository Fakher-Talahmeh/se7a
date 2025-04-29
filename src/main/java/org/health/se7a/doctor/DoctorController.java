package org.health.se7a.doctor;

import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;
import org.health.se7a.common.XppResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Page<DoctorDTO>> getAllDoctors(Pageable pageable) {
        return XppResponseEntity.map(doctorService.getAllDoctors(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.userCanViewDoctorDetails(#id)")
    public XppResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return XppResponseEntity.map(doctorService.getDoctorById(id));
    }

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Boolean> createDoctor(@RequestBody @Validated(OnCreate.class) DoctorDTO doctorDTO) {
        Boolean createdDoctor = doctorService.createDoctor(doctorDTO);
        return XppResponseEntity.map(createdDoctor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.userCanViewDoctorDetails(#id)")
    public XppResponseEntity<Boolean> updateDoctor(
            @PathVariable Long id,
            @RequestBody  @Validated(OnUpdate.class) DoctorDTO doctorDTO) {
        doctorDTO.setId(id);
        Boolean updatedDoctor = doctorService.updateDoctor(doctorDTO);
        return XppResponseEntity.map(updatedDoctor);
    }
    @GetMapping("/lookup")
    public XppResponseEntity<List<DoctorLookupDTO>> getDoctorLookup() {
        return XppResponseEntity.map(doctorService.getAllDoctorsForLookup());
    }

    @GetMapping("/today")
    @PreAuthorize("@authorizationService.loggedInUserIsDoctor()")
    public XppResponseEntity<Map<String, String>> getAllVisitsToday() {
        Map<String, String> visitsToday = doctorService.getAllVisitsToday();
        return XppResponseEntity.map(visitsToday);
    }

}
