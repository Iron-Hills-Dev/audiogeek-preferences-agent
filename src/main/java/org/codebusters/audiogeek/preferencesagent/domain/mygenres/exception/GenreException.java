package org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.codebusters.audiogeek.preferencesagent.domain.exception.ErrorData;

@Getter
@Accessors(fluent = true)
public class GenreException extends RuntimeException {
    private final String code;
    private final String message;

    public GenreException(ErrorData data, Object... args) {
        code = data.code();
        message = data.message().formatted(args);
    }

    @Getter
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    public enum GenreExceptionData implements ErrorData {
        TOO_LONG("GE_TL", "Genre is too long: %s>%s"),
        ILLEGAL_CHAR("GE_IC", "Genre contains illegal character: %s"),
        BLANK("GE_B", "Provided genre is blank");
        private final String code;
        private final String message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
