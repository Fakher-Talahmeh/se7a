package org.health.se7a.security.provider;

import org.health.se7a.exception.XppAuthenticationException;
import org.health.se7a.security.model.VerifyAuthenticationToken;
import org.health.se7a.security.otp.OTP;
import org.health.se7a.security.otp.OTPService;
import org.health.se7a.security.otp.OTPStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.health.se7a.exception.XppAuthenticationException.tooManyVerifications;
import static org.health.se7a.exception.XppAuthenticationException.wrongCode;
import static org.health.se7a.security.otp.OTPStatus.*;

@Service
@RequiredArgsConstructor
public class VerifyCodeAuthenticationProvider implements AuthenticationProvider {

    private final OTPService otpService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        VerifyAuthenticationToken verifyAuthenticationToken = (VerifyAuthenticationToken) authentication;
        checkIfUserCanVerifyCode(verifyAuthenticationToken);
        otpService.verify(verifyAuthenticationToken.getUuid());
        OTP otp = otpService.findByUserSecurityIdAndUuid(verifyAuthenticationToken.getName(), verifyAuthenticationToken.getUuid());
        if (!isCorrectCode(verifyAuthenticationToken, otp))
            throw wrongCode();
        else return verifyAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return VerifyAuthenticationToken.class.equals(authentication);
    }

    private void checkIfUserCanVerifyCode(VerifyAuthenticationToken verifyAuthenticationToken) {
        OTPStatus status = otpService.findByUserSecurityIdAndUuid(verifyAuthenticationToken.getName(), verifyAuthenticationToken.getUuid())
                .getOtpStatus();
        if (List.of(VERIFIED, INVALID).contains(status))
            throw XppAuthenticationException.builder()
                    .message(String.format("Code's status should be %s to be able to verify it. This code status is %s", INITIALIZED, status))
                    .build();
        if (!(otpService.countNumberOfVerificationsByUuid(verifyAuthenticationToken.getUuid()) <= 3))
            invalidateAndThrowException(verifyAuthenticationToken);
    }

    private Boolean isCorrectCode(VerifyAuthenticationToken token, OTP otp) {
        return otp.getCode().equals(token.getCode());
    }

    private void invalidateAndThrowException(VerifyAuthenticationToken verifyAuthenticationToken) {
        otpService.setOtpStatus(verifyAuthenticationToken.getUuid(), OTPStatus.INVALID);
        throw tooManyVerifications();
    }
}
