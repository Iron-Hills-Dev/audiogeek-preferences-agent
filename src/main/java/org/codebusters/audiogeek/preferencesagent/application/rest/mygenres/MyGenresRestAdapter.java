package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.application.auth.token.HuellTokenPayload;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.GetMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.PutMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
class MyGenresRestAdapter {

    private final GetMyGenresPort getMyGenresPort;
    private final PutMyGenresPort putMyGenresPort;
    private final GenreConverter genreConverter;

    /**
     * Function handling /my-genres GET request
     * @param token token authorizing user
     * @param authentication token payload via Spring Security
     * @return 200 OK and GetMyGenresResponse
     */
    @GetMapping(value = "/my-genres", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetMyGenresResponse> getMyGenres(@RequestHeader("Authorization") String token, Authentication authentication) {
        log.info("Processing GET request /my-genres");
        var payload = (HuellTokenPayload) authentication.getPrincipal();
        return ResponseEntity.ok(new GetMyGenresResponse(genreConverter.toString(getMyGenresPort.getMyGenres(new UserID(payload.id())))));
    }

    /**
     * Fuction handling /my-genres PUT request
     * @param request request data (PutMyGenresRequest)
     * @param token token authorizing user
     * @param authentication token payload via Spring Security
     * @return 201 CREATED (set genres for user)
     */
    @PutMapping(value = "/my-genres", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putMyGenres(@RequestBody PutMyGenresRequest request,
                                            @RequestHeader("Authorization") String token,
                                            Authentication authentication) {
        var payload = (HuellTokenPayload) authentication.getPrincipal();
        log.info("Processing POST request /my-genres: request={}", request);
        putMyGenresPort.putMyGenres(new UserID(payload.id()), genreConverter.toGenres(request.genres()));
        return ResponseEntity.status(CREATED).build();
    }
}
