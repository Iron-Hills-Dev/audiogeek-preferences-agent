package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.GetMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.PutMyGenresPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    //TODO use correct ID after integration with Spring Security
    private static final UserID DUMMY_ID = new UserID(UUID.fromString("45ca6b95-6cf3-4aca-b651-84a2a2aa8cd7"));

    @GetMapping(value = "/my-genres", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetMyGenresResponse> getMyGenres(@RequestHeader("Authorization") String token) {
        log.info("Processing GET request /my-genres");
        return ResponseEntity.ok(new GetMyGenresResponse(genreConverter.toString(getMyGenresPort.getMyGenres(DUMMY_ID))));
    }

    @PutMapping(value = "/my-genres", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putMyGenres(@RequestBody PutMyGenresRequest request,
                                            @RequestHeader("Authorization") String token) {
        log.info("Processing POST request /my-genres: request={}", request);
        putMyGenresPort.putMyGenres(DUMMY_ID, genreConverter.toGenres(request.genres()));
        return ResponseEntity.status(CREATED).build();
    }
}
