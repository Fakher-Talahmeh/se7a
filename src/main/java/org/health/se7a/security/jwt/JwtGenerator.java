package org.health.se7a.security.jwt;

import org.health.se7a.util.DateUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.health.se7a.util.DateUtil.convertMinToMs;

@Service
public class JwtGenerator {
    private static final String secretKey = "69d0a4196f887b1393a18fb7ff408dbed74e913e7d7ed05e00c36aa869d1c3b6";

    public String generateJwtToken(JwtRequest request) {
        Key key = generateKey();
        return Jwts.builder()
                .issuedAt(DateUtil.now())
                .expiration(DateUtil.expAt(convertMinToMs(request.getExpiresAfterMin())))
                .subject(request.getSubject())
                .claims(request.getClaims())
                .signWith(key)
                .compact();
    }

    public static SecretKey generateKey() {
        byte[] apiKeySecretBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    private Date toDate(Long ms) {
        return new Date(System.currentTimeMillis() + ms);
    }
}
