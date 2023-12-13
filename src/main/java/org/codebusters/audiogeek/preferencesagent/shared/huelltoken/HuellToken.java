package org.codebusters.audiogeek.preferencesagent.shared.huelltoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

import static io.jsonwebtoken.Jwts.parser;
import static org.codebusters.audiogeek.preferencesagent.shared.huelltoken.ErrorData.*;


@Slf4j
@RequiredArgsConstructor
public class HuellToken {
    private final String secret;
    private final String algorithm;

    /**
     * Parses given JWT with rules of Huell
     *
     * @param token JWT
     * @return Parsed JWT
     * @throws HuellTokenException if token is not valid
     */
    public HuellTokenPayload parse(String token) {
        log.debug("Parsing Huell Token");
        try {
            return convertToPayload(parseJwt(token));
        } catch (ExpiredJwtException e) {
            log.warn("Token is expired");
            throw new HuellTokenException(EXPIRED_TOKEN, "" + (System.currentTimeMillis() - e.getClaims().getExpiration().getTime()));
        } catch (SignatureException e) {
            log.warn("Token is signed with wrong signature");
            throw new HuellTokenException(WRONG_SIGNATURE);
        } catch (IllegalArgumentException e) {
            log.warn("Token has incorrect payload");
            throw new HuellTokenException(WRONG_PAYLOAD);
        }
    }

    /**
     * Parses JWT using Java JWT library
     *
     * @param jwt JWT
     * @return Payload in Claims
     */
    private Jws<Claims> parseJwt(String jwt) {
        log.trace("Parsing JWT: algorithm={}", algorithm);
        var KEY = new SecretKeySpec(secret.getBytes(), algorithm);
        return parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwt);
    }

    /**
     * Converts Claims payload with rules of Huell
     *
     * @param claims parsed JWT
     * @return Readable class
     */
    private HuellTokenPayload convertToPayload(Jws<Claims> claims) {
        log.trace("Converting to payload");
        return new HuellTokenPayload(
                UUID.fromString(claims.getPayload().get("user_id", String.class)));
    }

}