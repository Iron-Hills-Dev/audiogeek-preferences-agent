package org.codebusters.audiogeek.preferencesagent.application.auth;

import org.codebusters.audiogeek.preferencesagent.shared.huelltoken.HuellToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, HuellToken tokenApi) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c.anyRequest().fullyAuthenticated())
                .exceptionHandling(c -> c.authenticationEntryPoint(new ApiAuthFailureHandler()))
                .addFilterBefore(new UserAuthFilter(tokenApi), SecurityContextHolderAwareRequestFilter.class)
                .sessionManagement(c -> c.sessionCreationPolicy(STATELESS))
                .build();

    }

}
