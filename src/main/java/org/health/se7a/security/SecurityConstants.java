package org.health.se7a.security;

import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpMethod.GET;

public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String LOGIN_URL = "/login";
    public static final String VERIFY_URL = "/verify";

    public static final String RESEND_URL = "/resend";

    public static final String TEMP_ROLE_CODE = "ROLE_TEMP";

    public static final String UUID_KEY_NAME = "uuid";
    public static final String BEARER_AUTH_HEADER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String ACCESS_TOKEN_HEADER_NAME = "accessToken";

    public static final Map<String, List<HttpMethod>> UNPROTECTED_ENDPOINTS_MAP = new ConcurrentHashMap<>() {{
        put("/", List.of(GET));
    }};


    public static final List<String> AUTHENTICATION_URLS = List.of(
            LOGIN_URL,
            VERIFY_URL,
            RESEND_URL
    );

    public static final List<String> UNPROTECTED_ENDPOINTS = List.of(
            "/register/driver",
            "/register/passenger"
    );

}
