package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresModifyPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.PutGenresCmd;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.user.UserID;
import org.codebusters.audiogeek.preferencesagent.application.util.GenreUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.codebusters.audiogeek.preferencesagent.application.auth.AuthUtility.extractHuellPayload;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
class MyGenresRestAdapter {

    private final MyGenresQueryPort myGenresQueryPort;
    private final MyGenresModifyPort myGenresModifyPort;
    private final GenreUtils genreUtils;

    /**
     * Function handling /my-genres GET request
     *
     * @param token          token authorizing user
     * @param authentication token payload via Spring Security
     * @return 200 OK and GetMyGenresResponse
     */
    @GetMapping(value = "/my-genres", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetMyGenresResponse> getMyGenres(@RequestHeader("Authorization") String token, Authentication authentication) {
        log.info("Processing GET request /my-genres");
        var payload = extractHuellPayload(authentication);
        return ResponseEntity.ok(new GetMyGenresResponse(genreUtils.genresToStrings(myGenresQueryPort.getMyGenres(new UserID(payload.id())))));
    }

    /**
     * Function handling /my-genres PUT request
     *
     * @param request        request data (PutMyGenresRequest)
     * @param token          token authorizing user
     * @param authentication token payload via Spring Security
     * @return 201 CREATED (set genres for user)
     */
    @PutMapping(value = "/my-genres", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putMyGenres(@RequestBody PutMyGenresRequest request,
                                            @RequestHeader("Authorization") String token,
                                            Authentication authentication) {
        var payload = extractHuellPayload(authentication);
        log.info("Processing POST request /my-genres: request={}", request);
        myGenresModifyPort.putMyGenres(PutGenresCmd.builder()
                .id(new UserID(payload.id()))
                .genres(genreUtils.stringsToGenres(request.genres()))
                .build());
        return ResponseEntity.status(CREATED).build();
    }
}
