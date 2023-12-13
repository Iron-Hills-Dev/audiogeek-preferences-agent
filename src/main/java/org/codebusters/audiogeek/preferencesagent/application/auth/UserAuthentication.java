package org.codebusters.audiogeek.preferencesagent.application.auth;

import org.codebusters.audiogeek.preferencesagent.shared.huelltoken.HuellTokenPayload;
import org.springframework.security.authentication.AbstractAuthenticationToken;

class UserAuthentication extends AbstractAuthenticationToken {
    private final HuellTokenPayload principal;

    public UserAuthentication(HuellTokenPayload principal) {
        super(null);
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
