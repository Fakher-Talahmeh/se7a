package org.health.se7a.security.util;

import org.health.se7a.doctor.Doctor;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.security.model.AuthenticatedUserToken;
import org.health.se7a.security.model.LoggedUserDto;
import org.health.se7a.security.model.LoginType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {

    public static Doctor defultDoctor() {
        return Doctor.builder()
                .id(1L)
                .build();
    }
    public static Nurse defultNurse() {
        return Nurse.builder()
                .id(1L)
                .build();
    }
    public static LoggedUserDto defaultAdmin(){
        return LoggedUserDto.builder()
                .type(LoginType.ADMIN)
                .username("admin")
                .id(1L)
                .build();
    }

    public static LoggedUserDto loggedUser() {
        AuthenticatedUserToken token = (AuthenticatedUserToken) SecurityContextHolder.getContext().getAuthentication();
        LoginType loginType = token.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(LoginType::valueOf)
                .orElseThrow(() -> new RuntimeException("Unidentified role"));
        return LoggedUserDto.builder()

                .id(token.getId())
                .type(loginType)
                .build();
    }
}
