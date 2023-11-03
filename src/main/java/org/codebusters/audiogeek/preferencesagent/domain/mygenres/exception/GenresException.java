package org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.codebusters.audiogeek.preferencesagent.domain.exception.ErrorData;

@Getter
@Accessors(fluent = true)
public class GenresException extends RuntimeException {
    private final String code;
    private final String message;

    public GenresException(ErrorData data) {
        code = data.code();
        message = data.message();
    }

    @Getter
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    public enum GenreExceptionData implements ErrorData {
        // TODO add genre errors
        DUMMY_ERROR("", "");
        private final String code;
        private final String message;
    }
}
