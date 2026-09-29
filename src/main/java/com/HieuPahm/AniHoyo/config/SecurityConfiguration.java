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
                // Public paths live in PublicPaths so the filter chain and the
                // AuthorityIntercepter ACL cannot drift apart (they did: the view
                // counter and the HLS playlists were permitAll here but were then
                // refused with 403 by the ACL for logged-in users).
                http
                                .csrf(c -> c.disable())
                                .cors(Customizer.withDefaults()) // This will use the CorsConfigure bean
                                .authorizeHttpRequests(
                                                authz -> authz
                                                                .requestMatchers(PublicPaths.PUBLIC).permitAll()
                                                                // Ghi chú: nếu muốn cho khách chưa đăng nhập xem
                                                                // nội dung, thêm "/api/v1/seasons/**" (GET) vào
                                                                // PublicPaths.PUBLIC_PLAYBACK.

                                                                .requestMatchers(HttpMethod.POST,
                                                                                PublicPaths.PUBLIC_VIEW_TRACKING)
                                                                .permitAll()
                                                                .requestMatchers(HttpMethod.GET,
                                                                                PublicPaths.PUBLIC_PLAYBACK)
                                                                .permitAll()
                                                                .anyRequest().authenticated())
                                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                                                .authenticationEntryPoint(authEntryPointConfig))
                                // Default config

                                .formLogin(f -> f.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return http.build();
        }
}
