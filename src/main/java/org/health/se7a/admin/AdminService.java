package org.health.se7a.admin;


import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.doctor.DoctorRepository;
import org.health.se7a.entity.EntityService;
import org.health.se7a.exception.XppException;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.nurse.NurseRepository;
import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import org.health.se7a.security.service.LoginDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final LoginDetailsServiceImpl loginDetailsService;
    private final EntityService entityService;

    @PostConstruct
    @Transactional
    public void initAdmin() {
        if (adminRepository.count() == 0) {
            Admin defaultAdmin = Admin.builder()
                    .name("Admin")
                    .telNumber("0599078888")
                    .accountStatus(AccountStatus.ACTIVE)
                    .build();

            adminRepository.save(defaultAdmin);
            entityService.addUserLoginInfo(defaultAdmin.getTelNumber(), LoginType.ADMIN);

            log.info("Default Admin Created Successfully!");
        } else {
            log.info("Admin already exists. Skipping creation.");
        }
    }


    public void setAccountStatus(String phoneNumber,LoginType loginType,AccountStatus accountStatus) {
        System.out.println(loginType);
        UserRepository repository = loginDetailsService.getRepositoryByLoginType(loginType);
        LoginUser user = repository.findByTelNumber(phoneNumber)
                .orElseThrow(() -> new XppException(List.of(phoneNumber),
                        HttpStatus.NOT_FOUND,
                        "user.not.found"
                ));
        user.setAccountStatus(accountStatus);
        System.out.println(user.getType());
        if (user.getType() == LoginType.DOCTOR) {
            ((DoctorRepository) repository).save((Doctor) user);
        } else if (user.getType() == LoginType.NURSE) {
            ((NurseRepository) repository).save((Nurse) user);
        }
    }
}
