package org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception;

import lombok.Getter;

import static java.lang.String.format;


@Getter
public class MyGenresDomainException extends RuntimeException {
    public static final String CODE_PREFIX = "MGD_";
    private final MyGenresDomainErrorData data;
    private final Object[] params;
    private final String code;
    private final String message;

    public MyGenresDomainException(MyGenresDomainErrorData data, Object... params) {
        this.data = data;
        this.params = params;
        this.code = data.getCode();
        this.message = format(data.getMessage(), params);
    }
}
