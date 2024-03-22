package org.codebusters.audiogeek.preferencesagent.application.auth;

import org.codebusters.audiogeek.preferencesagent.shared.huelltoken.HuellTokenPayload;
import org.springframework.security.core.Authentication;

public class AuthUtility {
    public static HuellTokenPayload extractHuellPayload(Authentication auth) {
        return (HuellTokenPayload) auth.getPrincipal();
    }
}
