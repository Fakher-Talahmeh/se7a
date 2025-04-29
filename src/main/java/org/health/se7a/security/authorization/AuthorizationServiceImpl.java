package org.health.se7a.security.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.util.SecurityContextUtil;
import org.springframework.stereotype.Service;
@Service("authorizationService")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public Boolean userCanViewDoctorDetails(Long doctorId) {
        if (loggedInUserIsAdmin())
            return true;
        if (loggedInUserIsDoctor())
            return loggedInUserId().equals(doctorId);
        else return false;
    }

    @Override
    public Boolean userCanViewNurseDetails(Long nurseId) {
        if (loggedInUserIsAdmin())
            return true;
        if (loggedInUserIsNurse())
            return loggedInUserId().equals(nurseId);
        else return false;
    }

    @Override
    public Boolean loggedInUserIsAdminOrDoctor() {
        return loggedInUserIsAdmin() || loggedInUserIsDoctor();
    }


    @Override
    public Boolean loggedInUserIsAdminOrSecretary() {
        return loggedInUserIsAdmin() || loggedInUserIsSecretary();
    }


    @Override
    public Boolean loggedInUserIsAdminOrNurse() {
        return loggedInUserIsAdmin() || loggedInUserIsNurse();
    }

    @Override
    public Boolean userCanViewSecretaryDetails(Long secretaryId) {
        if (loggedInUserIsAdmin())
            return true;
        if (loggedInUserIsSecretary())
            return loggedInUserId().equals(secretaryId);
        else return false;
    }

    @Override
    public Boolean loggedInUserIsNurse() {
        return loggedInUserIsOfType(LoginType.NURSE);
    }

    @Override
    public Boolean loggedInUserIsSecretary() {
        return loggedInUserIsOfType(LoginType.SECRETARY);
    }

    @Override
    public Boolean loggedInUserIsAdmin() {
        return loggedInUserIsOfType(LoginType.ADMIN);
    }

    @Override
    public Boolean loggedInUserIsDoctor() {
        return loggedInUserIsOfType(LoginType.DOCTOR);
    }



    @Override
    public Boolean userCanViewVitalSigns(Long patientId) {
        if (loggedInUserIsAdmin() || loggedInUserIsDoctor()) {
            return true;
        }
        return SecurityContextUtil.loggedUser().getId().equals(patientId);
    }

    private Boolean loggedInUserIsOfType(LoginType type) {
        return SecurityContextUtil.loggedUser()
                .getType()
                .equals(type);
    }

    private Long loggedInUserId() {
        return SecurityContextUtil.loggedUser().getId();
    }
}
