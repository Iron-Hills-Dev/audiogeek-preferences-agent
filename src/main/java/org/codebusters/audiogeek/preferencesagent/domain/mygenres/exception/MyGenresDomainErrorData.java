package org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.codebusters.audiogeek.preferencesagent.infrastructure.exception.ErrorData;
import org.springframework.http.HttpStatus;

import static org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.MyGenresDomainException.CODE_PREFIX;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;


@Getter
@RequiredArgsConstructor
public enum MyGenresDomainErrorData implements ErrorData {
    GENRE_ERROR_TEST(CODE_PREFIX + "ET", "This is a test error message, param: %s", I_AM_A_TEAPOT),
    GENRE_EMPTY(CODE_PREFIX + "GE", "One of genres is empty", NOT_ACCEPTABLE);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}



