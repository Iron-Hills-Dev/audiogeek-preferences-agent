package org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception;

public class UserNotFoundException extends RuntimeException {
    public final String code = "UDNE";
    public final String message = "User do not exists";

    @Override
    public String getMessage() {
        return message;
    }
}
