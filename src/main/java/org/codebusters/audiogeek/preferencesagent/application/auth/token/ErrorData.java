package org.codebusters.audiogeek.preferencesagent.application.auth.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ErrorData {
    EXPIRED_TOKEN("Token has expired %s milliseconds ago"),
    WRONG_SIGNATURE("Token has wrong signature"),
    WRONG_PAYLOAD("Payload do not match with pattern");

    private final String message;
}
