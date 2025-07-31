package com.HieuPahm.AniHoyo.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.dtos.auth.ResLoginDTO;

@Service
public class SecurityUtils {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    private final JwtEncoder jwtEncoder;

    @Value("${anihoyo.jwt.base64-secret}")
    private String jwtKey;

    @Value("${anihoyo.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpire;
    @Value("${anihoyo.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpire;

    public SecurityUtils(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }
     /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }
    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
    // =========================
    
    public String generateAccessToken(Authentication authentication){
        // assign to claim
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpire, ChronoUnit.SECONDS);
 
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(authentication.getName())
        .claim("user account", authentication)
        .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
    public String generateRefreshToken(String emailLogin, ResLoginDTO resLoginDTO){
        ResLoginDTO.InfoWithinToken data = new ResLoginDTO.InfoWithinToken();
        data.setId(resLoginDTO.getUser().getId());
        data.setEmail(resLoginDTO.getUser().getEmail());
        data.setName(resLoginDTO.getUser().getName());
         // assign to claim
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpire, ChronoUnit.SECONDS);
        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(emailLogin)
        .claim("user account", data)
        .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
