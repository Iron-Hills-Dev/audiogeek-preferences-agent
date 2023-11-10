package org.codebusters.audiogeek.preferencesagent.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.codebusters.audiogeek.preferencesagent.domain.exception.ErrorData;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ApiErrorData implements ErrorData {
    NOT_AUTHENTICATED("API_NA", "Not authenticated request.", UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
