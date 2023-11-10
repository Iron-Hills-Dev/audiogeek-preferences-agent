package org.codebusters.audiogeek.preferencesagent.application.auth.token;

import java.util.UUID;

/**
 * Payload of Huell Token
 * @param id UUID of user
 */
public record HuellTokenPayload(UUID id) {
}
