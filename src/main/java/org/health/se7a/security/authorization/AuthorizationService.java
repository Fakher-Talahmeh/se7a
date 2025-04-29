package org.health.se7a.security.authorization;

public interface AuthorizationService {

    Boolean loggedInUserIsAdmin();

    Boolean loggedInUserIsDoctor();

    Boolean loggedInUserIsNurse();

    Boolean userCanViewDoctorDetails(Long doctorId);

    Boolean userCanViewNurseDetails(Long nurseId);

    Boolean loggedInUserIsSecretary();

    Boolean userCanViewSecretaryDetails(Long secretaryId);

    Boolean loggedInUserIsAdminOrNurse();

    Boolean loggedInUserIsAdminOrDoctor();
    Boolean  loggedInUserIsAdminOrSecretary();

    Boolean userCanViewVitalSigns(Long patientId);
}
