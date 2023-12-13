package org.codebusters.audiogeek.preferencesagent.shared.huelltoken;

public class HuellTokenException extends RuntimeException {
    public final String message;

    HuellTokenException(ErrorData errorData, Object... args) {
        message = errorData.message().formatted(args);

    }

    @Override
    public String getMessage() {
        return message;
    }
}
