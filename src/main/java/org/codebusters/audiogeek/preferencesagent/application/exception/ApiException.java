package org.codebusters.audiogeek.preferencesagent.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * General exception used by application module
 */
@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public ApiException(ApiErrorData data) {
        this.code = data.code();
        this.message = data.message();
        this.httpStatus = data.status();
    }
}
