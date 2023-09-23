package org.codebusters.audiogeek.preferencesagent.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.codebusters.audiogeek.preferencesagent.infrastructure.exception.ExceptionModel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException implements ExceptionModel {
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
