package com.HieuPahm.AniHoyo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    AuthorityIntercepter getAuthorityInterceptor() {
        return new AuthorityIntercepter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**", "/storage/**",
                "/api/v1/files",
        };
        registry.addInterceptor(getAuthorityInterceptor()).excludePathPatterns(whiteList);
    }
}
