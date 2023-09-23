package org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception;

import org.codebusters.audiogeek.preferencesagent.infrastructure.exception.ErrorData;
import org.codebusters.audiogeek.preferencesagent.infrastructure.exception.ExceptionModel;
import org.springframework.http.HttpStatus;

import static java.lang.String.format;


public class MyGenresDomainException extends RuntimeException implements ExceptionModel {
    public static final String CODE_PREFIX = "MGD_";
    private final ErrorData data;
    private final Object[] params;

    public MyGenresDomainException(MyGenresDomainErrorData data, Object... params) {
        this.data = data;
        this.params = params;
    }

    @Override
    public String getCode() {
        return data.getCode();
    }

    @Override
    public String getMessage() {
        return format(data.getMessage(), params);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return data.getHttpStatus();
    }
}
