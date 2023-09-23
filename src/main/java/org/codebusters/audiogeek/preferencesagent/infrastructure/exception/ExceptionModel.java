package org.codebusters.audiogeek.preferencesagent.infrastructure.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionModel {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
