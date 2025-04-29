package org.health.se7a.security.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.health.se7a.util.Conditional.requireNonNullObjects;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final OTPRepository otpRepository;

    public OTP findByUserSecurityIdAndUuid(String userId, String uuid) {
        return otpRepository.findByUserSecurityIdAndUuid(userId, uuid)
                .orElse(new OTP());
    }

    public void create(String userId, String code, String uuid) {
        requireNonNullObjects(userId, code, uuid);
        OTP otp = new OTP();
        otp.setUserSecurityId(userId);
        otp.setCode(code);
        otp.setUuid(uuid);
        otp.setInitializedAt(LocalDateTime.now());
        otp.setOtpStatus(OTPStatus.INITIALIZED);
        otp.setNumberOfVerifications(0);
        otpRepository.save(otp);
    }

    public void verify(String uuid) {
        otpRepository.findByUuid(uuid)
                .map(temp -> {
                    temp.setNumberOfVerifications(temp.getNumberOfVerifications() + 1);
                    return temp;
                }).map(otpRepository::save);
    }

    public void setOtpStatus(String uuid, OTPStatus otpStatus) {
        otpRepository.findByUuid(uuid)
                .map(temp -> {
                    temp.setOtpStatus(otpStatus);
                    return temp;
                }).map(otpRepository::save);
    }

    public Integer countRecentOTPsByUser(String userId) {
        return otpRepository.countByUserSecurityIdAndInitializedAtBetween(userId);
    }

    public Integer countNumberOfVerificationsByUuid(String uuid) {
        return otpRepository.findByUuid(uuid)
                .map(OTP::getNumberOfVerifications)
                .orElse(0);
    }
}
