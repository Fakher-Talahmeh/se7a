package org.health.se7a.nurse;

import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;
import org.health.se7a.common.XppResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nurses")
@RequiredArgsConstructor
public class NurseController {

    private final NurseService nurseService;

    @GetMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Page<NurseDTO>> getAllNurses(Pageable pageable) {
        return XppResponseEntity.map(nurseService.getAllNurses(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.userCanViewNurseDetails(#id)")
    public XppResponseEntity<NurseDTO> getNurseById(@PathVariable Long id) {
        return XppResponseEntity.map(nurseService.getNurseById(id));
    }

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Boolean> createNurse(@RequestBody  @Validated(OnCreate.class) NurseDTO nurseDTO) {
        Boolean createdNurse = nurseService.createNurse(nurseDTO);
        return XppResponseEntity.map(createdNurse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.userCanViewNurseDetails(#id)")
    public XppResponseEntity<Boolean> updateNurse(
            @PathVariable Long id,
            @RequestBody  @Validated(OnUpdate.class) NurseDTO nurseDTO) {
        nurseDTO.setId(id);
        Boolean updatedNurse = nurseService.updateNurse(nurseDTO);
        return XppResponseEntity.map(updatedNurse);
    }


}
