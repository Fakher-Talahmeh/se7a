package org.health.se7a.secretary;

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
@RequestMapping("/secretaries")
@RequiredArgsConstructor
public class SecretaryController {

    private final SecretaryService secretaryService;

    @GetMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Page<SecretaryDTO>> getAllSecretaries(Pageable pageable) {
        return XppResponseEntity.map(secretaryService.getAllSecretaries(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.userCanViewSecretaryDetails(#id)")
    public XppResponseEntity<SecretaryDTO> getSecretaryById(@PathVariable Long id) {
        return XppResponseEntity.map(secretaryService.getSecretary(id));
    }

    @PostMapping
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Boolean> createSecretary(@RequestBody @Validated(OnCreate.class) SecretaryDTO secretaryDTO) {
        Boolean createdSecretary = secretaryService.createSecretary(secretaryDTO);
        return XppResponseEntity.map(createdSecretary);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.userCanViewSecretaryDetails(#id)")
    public XppResponseEntity<Boolean> updateSecretary(
            @PathVariable Long id,
            @RequestBody @Validated(OnUpdate.class) SecretaryDTO secretaryDTO) {
        secretaryDTO.setId(id);
        Boolean updatedSecretary = secretaryService.updateSecretary(secretaryDTO);
        return XppResponseEntity.map(updatedSecretary);
    }


}
