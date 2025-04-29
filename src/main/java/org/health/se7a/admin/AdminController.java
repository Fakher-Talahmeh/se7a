package org.health.se7a.admin;

import jakarta.validation.Valid;
import org.health.se7a.common.XppResponseEntity;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/root")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/account/status")
    @PreAuthorize("@authorizationService.loggedInUserIsAdmin()")
    public XppResponseEntity<Void> getCarByPlateNumber(@RequestBody @Validated StatusDTO statusDTO ) {
       adminService.setAccountStatus(statusDTO.getPhoneNumber(),statusDTO.getType(),statusDTO.getStatus());
        return XppResponseEntity.map(HttpStatus.OK);
    }
}
