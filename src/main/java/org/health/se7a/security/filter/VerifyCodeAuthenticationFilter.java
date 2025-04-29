package org.health.se7a.security.filter;

import org.health.se7a.security.jwt.JwtUtil;
import org.health.se7a.security.model.VerifyAuthenticationToken;
import org.health.se7a.security.model.VerifyCodeRequest;
import org.health.se7a.util.HttpObjectParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

import static org.health.se7a.exception.XppAuthenticationException.invalidToken;
import static org.health.se7a.security.SecurityConstants.VERIFY_URL;
import static org.health.se7a.security.jwt.JwtUtil.extractJwtTokenFromRequest;
import static org.health.se7a.security.jwt.JwtUtil.validateTempToken;

public class VerifyCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public VerifyCodeAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(VERIFY_URL, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        String jwtToken = extractJwtTokenFromRequest(request);
        if (!validateTempToken(jwtToken)) {
            throw invalidToken();
        }

        VerifyCodeRequest verifyCodeRequest = HttpObjectParser.parseInputStream(request.getInputStream(), VerifyCodeRequest.class);
        VerifyAuthenticationToken verifyAuthenticationToken = VerifyAuthenticationToken.builder()
                .code(verifyCodeRequest.getCode())
                .username(JwtUtil.extractClaimsFromToken(jwtToken).getSubject())
                .uuid(JwtUtil.extractClaimsFromToken(jwtToken).get("uuid").toString())
                .build();
        return getAuthenticationManager().authenticate(verifyAuthenticationToken);
    }


}
