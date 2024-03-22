package org.codebusters.audiogeek.preferencesagent.shared.huelltoken;

import java.util.UUID;

/**
 * Payload of Huell Token
 * @param id UUID of user
 */
public record HuellTokenPayload(UUID id) {
}
