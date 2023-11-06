package org.codebusters.audiogeek.preferencesagent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.codebusters.audiogeek.preferencesagent.domain.exception.ErrorData;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum TestErrorData implements ErrorData {
    GENRE_ERROR_TEST("GENRE_TEST", "This is a test error");
    private final String code;
    private final String message;
}
