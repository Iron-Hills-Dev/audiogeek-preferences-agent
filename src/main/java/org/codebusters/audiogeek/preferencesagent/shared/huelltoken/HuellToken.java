package org.codebusters.audiogeek.preferencesagent.shared.huelltoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.UUID;

import static org.codebusters.audiogeek.preferencesagent.shared.huelltoken.ErrorData.*;


@Slf4j
@RequiredArgsConstructor
public class HuellToken {
    private final String secret;

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
        } catch (WeakKeyException e) {
            log.warn("Signature is too weak");
            throw new HuellTokenException(WEAK_SIGNATURE);
        }
    }

    /**
     * Parses JWT
     *
     * @param jwt token
     * @return Claims of token
     */
    private Jws<Claims> parseJwt(String jwt) {
        log.trace("Parsing JWT");
        var secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt);
    }

    /**
     * Converts Claims payload by rules of Huell
     *
     * @param claims parsed JWT in Claims
     * @return Readable class
     */
    private HuellTokenPayload convertToPayload(Jws<Claims> claims) {
        log.trace("Converting to payload");
        try {
            return new HuellTokenPayload(
                    UUID.fromString(claims.getPayload().get("user_id", String.class)));
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new HuellTokenException(WRONG_PAYLOAD);
        }

    }

}