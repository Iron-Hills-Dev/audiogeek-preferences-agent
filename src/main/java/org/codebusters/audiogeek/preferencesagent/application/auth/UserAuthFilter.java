package org.codebusters.audiogeek.preferencesagent.application.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.application.auth.token.HuellToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Spring Security filter that validates if given JWT is valid token and registers Principal in Security Context.
 */
@RequiredArgsConstructor
@Slf4j
class UserAuthFilter extends OncePerRequestFilter {
    private final HuellToken huellToken;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        var ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(null);
        log.info("Authenticating request: {}", request.getRequestURL());

        ofNullable(request.getHeader(AUTHORIZATION))
                .map(t -> t.replace("Bearer ", ""))
                .ifPresentOrElse(t -> registerPrincipal(t, ctx), () -> log.warn("No Auth header"));
        filterChain.doFilter(request, response);
    }

    private void registerPrincipal(String token, SecurityContext ctx) {
        try {
            ctx.setAuthentication(new UserAuthentication(huellToken.parse(token)));
        } catch (Exception e) {
            log.warn("Exception occurred while parsing token: token=\"{}\" e=\"{}\"", token, e.toString());
        }
    }
}
