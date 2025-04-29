package org.health.se7a.security.filter;

import org.health.se7a.security.SecurityConstants;
import org.health.se7a.security.jwt.JwtUtil;
import org.health.se7a.security.model.AuthenticatedUserToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.health.se7a.exception.XppAuthenticationException.invalidToken;
import static org.health.se7a.security.SecurityConstants.AUTHENTICATION_URLS;
import static org.health.se7a.security.jwt.JwtUtil.*;

@Order(value = 10)
@RequiredArgsConstructor
@Component
public class AuthenticatedUserFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityConstants.UNPROTECTED_ENDPOINTS.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = extractJwtTokenFromRequest(request);
            if (!jwtUtil.validateToken(token))
                throw invalidToken();
            AuthenticatedUserToken authenticatedUserToken = AuthenticatedUserToken.builder()
                    .loginType(extractLoginTypeFromJwt(token))
                    .id(extractLoginNameFromJwt(token))
                    .build();
            authenticatedUserToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUserToken);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            authenticationEntryPoint.commence(request, response, new AuthenticationException(ex.getMessage()) {
            });
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return  AUTHENTICATION_URLS.contains(request.getContextPath()) ;
    }
}
