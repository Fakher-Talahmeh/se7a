package org.health.se7a.entity;

import jakarta.transaction.Transactional;
import org.health.se7a.security.model.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityService {
    @Autowired
    private UserLoginInfoRepository userLoginInfoRepository;

    @Transactional
    public void addUserLoginInfo(String telNumber, LoginType loginType) {
        if (!userLoginInfoRepository.existsByTelNumber(telNumber)) {
            UserLoginInfo loginInfo = UserLoginInfo.builder()
                    .telNumber(telNumber)
                    .loginType(loginType)
                    .build();
            userLoginInfoRepository.save(loginInfo);
        }
    }
    public LoginType getLoginType(String telNumber) {
        return userLoginInfoRepository.getLoginTypeByTelNumber(telNumber);
    }

    @Transactional
    public void updateUserLoginInfoPhone(String oldPhone, String newPhone) {
        UserLoginInfo existing = userLoginInfoRepository.findByTelNumber(oldPhone)
                .orElseThrow(() -> new RuntimeException("User login info not found for phone: " + oldPhone));

        existing.setTelNumber(newPhone);
        userLoginInfoRepository.save(existing);
    }

}
