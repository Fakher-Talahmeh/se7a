package org.health.se7a.security.filter;

import org.health.se7a.security.model.ResendCodeAuthenticationToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

import static org.health.se7a.exception.XppAuthenticationException.invalidToken;
import static org.health.se7a.security.SecurityConstants.RESEND_URL;
import static org.health.se7a.security.SecurityConstants.UUID_KEY_NAME;
import static org.health.se7a.security.jwt.JwtUtil.*;

public class ResendCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public ResendCodeAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(RESEND_URL, authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String token = extractJwtTokenFromRequest(request);
        if (!validateTempToken(token))
            throw invalidToken();
        ResendCodeAuthenticationToken resendCodeAuthenticationToken = ResendCodeAuthenticationToken.builder()
                .uuid(extractClaimByKeyFromToken(token, UUID_KEY_NAME, String.class))
                .userId(extractSubjectFromToken(token))
                .loginType(extractLoginTypeFromJwt(token))
                .build();

        return getAuthenticationManager().authenticate(resendCodeAuthenticationToken);
    }

}
