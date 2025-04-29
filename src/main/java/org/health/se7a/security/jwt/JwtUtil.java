package org.health.se7a.security.jwt;

import org.health.se7a.exception.XppAuthenticationException;
import org.health.se7a.exception.XppException;
import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import org.health.se7a.security.service.LoginDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.health.se7a.security.SecurityConstants.*;
import static org.health.se7a.security.jwt.JwtGenerator.generateKey;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {
    private static final ConcurrentHashMap<String, Long> LOGGED_INVALID_TOKENS = new ConcurrentHashMap<>();
    private static final long LOG_SUPPRESSION_MS = 300_000;
    private final LoginDetailsServiceImpl loginDetailsService;

    public static Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public static String extractSubjectFromToken(String token) {
        return extractClaimsFromToken(token)
                .getSubject();
    }

    public static Boolean validateTempToken(String token) {
        try {
            return (Boolean) Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getOrDefault(TEMP_ROLE_CODE, false);
        } catch (Exception e) {
            return false;
        }
    }

    public  Boolean isActive(String token) {
        LoginType type = extractLoginTypeFromJwt(token);
        Long id = extractLoginNameFromJwt(token);
        UserRepository repository = loginDetailsService.getRepositoryByLoginType(type);
        LoginUser user = repository.loadById(id)
                .orElseThrow(() -> new XppException(List.of(id),
                        HttpStatus.NOT_FOUND,
                        "user.not.found"
                ));
        return user.getAccountStatus() == AccountStatus.ACTIVE;
    }

    public  Boolean validateToken(String token) {
        try {
            return isActive(token) && !(Boolean) Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getOrDefault(TEMP_ROLE_CODE, false)
                    ;
        } catch (ExpiredJwtException expiredJwtException) {
            if (shouldLogInvalidToken(token)) {
                log.error("Expired token");
            } return false;
        } catch (SignatureException signatureException) {
            if (shouldLogInvalidToken(token)) {
                log.error("Invalid signature");
            } return false;
        }
    }

    private static boolean shouldLogInvalidToken(String token) {
        Long lastLogged = LOGGED_INVALID_TOKENS.get(token);
        if (lastLogged == null || System.currentTimeMillis() - lastLogged > LOG_SUPPRESSION_MS) {
            LOGGED_INVALID_TOKENS.put(token, System.currentTimeMillis());
            return true;
        }
        return false;
    }

//    public static String extractJwtTokenFromRequest(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER_NAME))
//                .map(temp -> temp.replace(BEARER_AUTH_HEADER_PREFIX, "")
//                        .replace(" ", ""))
//                .orElseThrow(() -> new XppAuthenticationException("Invalid jwt header"));
//
//    }
    public static String extractJwtTokenFromRequest(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER_NAME))
                .map(temp -> temp.replace(BEARER_AUTH_HEADER_PREFIX, "").trim())
                .orElse(null);
        if (token != null) {
            return token;
        }
        String query = request.getQueryString();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2 && keyValue[0].equalsIgnoreCase("token")) {
                    return keyValue[1]; // Return the token from query parameters
                }
            }
        }
        throw new XppAuthenticationException("Invalid JWT: Token not found in header or query parameters");
    }


    public static <T> T extractClaimByKeyFromToken(String token, String key, Class<T> clazz) {
        try {
            return (T) extractClaimsFromToken(token)
                    .get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static LoginType extractLoginTypeFromJwt(String token) {
        return Optional.ofNullable(token)
                .map(JwtUtil::extractClaimsFromToken)
                .map(Claims::getSubject)
                .map(temp -> temp.split(":")[0])
                .map(LoginType::valueOf)
                .orElseThrow(() -> new XppAuthenticationException("Invalid login type"));
    }

    public static Long extractLoginNameFromJwt(String token) {
        return Optional.ofNullable(token)
                .map(JwtUtil::extractClaimsFromToken)
                .map(Claims::getSubject)
                .map(temp -> temp.split(":")[1])
                .map(Long::parseLong)
                .orElseThrow(() -> new XppAuthenticationException("Invalid login name"));
    }

    public static Long extractLoginIdFromUserId(String userId) {
        System.out.println(userId);
        return Optional.ofNullable(userId)
                .map(temp -> temp.split(":")[1])
                .map(Long::parseLong)
                .orElseThrow(() -> new XppAuthenticationException("Invalid user id"));
    }
}
