package org.codebusters.audiogeek.preferencesagent.application.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    private ResponseEntity<ErrorResponse> apiException(ApiException err) {
        return status(err.getHttpStatus())
                .body(new ErrorResponse(err.getCode(), err.getMessage()));
    }

}
