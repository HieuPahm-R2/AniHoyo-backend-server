package com.HieuPahm.AniHoyo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, AuthEntryPointConfig authEntryPointConfig)
                        throws Exception {
                String[] whileList = {
                                "/", "/api/v1/",
                                "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/register",
                                "/api/v1/stream/range/**",
                                "/storage/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                };
                http
                                .csrf(c -> c.disable())
                                .cors(Customizer.withDefaults()) // This will use the CorsConfigure bean
                                .authorizeHttpRequests(
                                                authz -> authz
                                                                .requestMatchers(whileList).permitAll()
                                                                .requestMatchers(HttpMethod.POST,
                                                                                "/api/v1/*/view/**")
                                                                .permitAll()
                                                                .requestMatchers(HttpMethod.GET,
                                                                                "/api/v1/*/master.m3u8")
                                                                .permitAll()
                                                                .requestMatchers(HttpMethod.GET, "/api/v1/*/*.ts")
                                                                .permitAll()
                                                                // Quality-specific playlist files
                                                                .requestMatchers(HttpMethod.GET,
                                                                                "/api/v1/*/*/index.m3u8")
                                                                .permitAll()
                                                                // HLS segments with quality folder
                                                                .requestMatchers(HttpMethod.GET,
                                                                                "/api/v1/*/*/*.ts")
                                                                .permitAll()
                                                                .anyRequest().authenticated())
                                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                                                .authenticationEntryPoint(authEntryPointConfig))
                                // Default config
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                                .formLogin(f -> f.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return http.build();
        }
}
