package org.codebusters.audiogeek.preferencesagent.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Interface for all enums containing error data
 */
public interface ErrorData {
    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
