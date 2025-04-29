package org.health.se7a.security;

import org.health.se7a.security.authorization.XppAccessDeniedHandler;
import org.health.se7a.security.filter.AuthenticatedUserFilter;
import org.health.se7a.security.filter.LoginAuthenticationFilter;
import org.health.se7a.security.filter.ResendCodeAuthenticationFilter;
import org.health.se7a.security.filter.VerifyCodeAuthenticationFilter;
import org.health.se7a.security.handler.login.LoginAuthenticationFailureHandler;
import org.health.se7a.security.handler.login.LoginAuthenticationSuccessHandler;
import org.health.se7a.security.handler.resend.ResendCodeFailureHandler;
import org.health.se7a.security.handler.resend.ResendCodeSuccessHandler;
import org.health.se7a.security.handler.verify.VerifyCodeFailureHandler;
import org.health.se7a.security.handler.verify.VerifyCodeSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final InMemoryAuthConfig inMemoryAuthConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LoginAuthenticationFilter loginAuthenticationFilter,
                                                   VerifyCodeAuthenticationFilter verifyCodeAuthenticationFilter,
                                                   ResendCodeAuthenticationFilter resendCodeAuthenticationFilter,
                                                   AuthenticatedUserFilter authenticatedUserFilter) throws Exception {

        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login", "/register/**", "/swagger-ui/**", "/v2/api-docs/**", "/swagger-resources/**", "/webjars/**","/swagger-ui.html","/favicon.ico")
                        .permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(e -> e.accessDeniedHandler(new XppAccessDeniedHandler()))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(authenticatedUserFilter, LoginAuthenticationFilter.class)
                .addFilterAt(verifyCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(resendCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        AuthenticationProvider inMemoryProvider = inMemoryAuthConfig.authenticationProvider();
        providers.add(inMemoryProvider);
        return new ProviderManager(providers);
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter(AuthenticationManager authenticationManager,
                                                               LoginAuthenticationSuccessHandler successHandler,
                                                               LoginAuthenticationFailureHandler failureHandler) {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    public VerifyCodeAuthenticationFilter verifyCodeFilter(AuthenticationManager authenticationManager,
                                                           VerifyCodeSuccessHandler verifyCodeSuccessHandler,
                                                           VerifyCodeFailureHandler verifyCodeFailureHandler) {
        VerifyCodeAuthenticationFilter filter = new VerifyCodeAuthenticationFilter(authenticationManager);
        filter.setAuthenticationSuccessHandler(verifyCodeSuccessHandler);
        filter.setAuthenticationFailureHandler(verifyCodeFailureHandler);
        return filter;
    }

    @Bean
    public ResendCodeAuthenticationFilter resendCodeFilter(AuthenticationManager manager,
                                                           ResendCodeSuccessHandler successHandler,
                                                           ResendCodeFailureHandler failureHandler) {
        ResendCodeAuthenticationFilter filter = new ResendCodeAuthenticationFilter(manager);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationSuccessHandler(successHandler);
        return filter;
    }
}
