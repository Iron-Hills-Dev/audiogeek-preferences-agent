package org.codebusters.audiogeek.preferencesagent.application.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.codebusters.audiogeek.preferencesagent.application.exception.ApiException;
import org.codebusters.audiogeek.preferencesagent.application.exception.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static org.codebusters.audiogeek.preferencesagent.application.exception.ApiErrorData.NOT_AUTHENTICATED;

class ApiAuthFailureHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, ApiException {
        response.setStatus(NOT_AUTHENTICATED.status().value());
        response.setContentType("application/json");

        var mapper = new ObjectMapper();
        try (var out = response.getWriter()) {
            out.write(mapper.writeValueAsString(new ErrorResponse(NOT_AUTHENTICATED.code(), NOT_AUTHENTICATED.message())));
            out.flush();
        }
    }
}
