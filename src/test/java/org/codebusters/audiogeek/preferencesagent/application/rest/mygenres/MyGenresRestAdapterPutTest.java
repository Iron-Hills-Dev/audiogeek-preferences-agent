package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresModifyPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.GenreException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.Set;

import static java.nio.file.Files.readAllBytes;
import static org.codebusters.audiogeek.preferencesagent.TestErrorData.GENRE_ERROR_TEST;
import static org.codebusters.audiogeek.preferencesagent.application.exception.ApiErrorData.NOT_AUTHENTICATED;
import static org.codebusters.audiogeek.preferencesagent.application.rest.mygenres.MyGenresRestAdapterGetTest.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("MyGenresRestAdapter - /my-genres PUT")
class MyGenresRestAdapterPutTest {

    private static final String PATH_PREFIX = "src/test/resources/application/rest/mygenres/";
    private static final Path CORRECT_REQUEST = Path.of(PATH_PREFIX + "put_request_correct.json");
    private static final Path GENRE_VALIDATION_ERROR_REQUEST = Path.of(PATH_PREFIX + "put_request_genre_validation_error.json");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MyGenresModifyPort myGenresModifyPort;
    @SpyBean
    private GenreFactory genreFactory;

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres PUT works correctly")
    void putMyGenresCorrect() throws Exception {
        // given
        Genre genre1 = genreFactory.createGenre("rock");
        Genre genre2 = genreFactory.createGenre("pop");
        doThrow(new AssertionError("Genres in body and genres given does not match"))
                .when(myGenresModifyPort)
                .putMyGenres(argThat(cmd ->
                        cmd.id().equals(TEST_ID) && !cmd.genres().equals(Set.of(genre1, genre2))));

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(CORRECT_REQUEST))
                        .header(AUTHORIZATION, TEST_TOKEN_CORRECT))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres PUT handles exception correctly")
    void putMyGenresError() throws Exception {
        // given
        doThrow(new GenreException(GENRE_ERROR_TEST)).when(genreFactory).createGenre("BAD");

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(GENRE_VALIDATION_ERROR_REQUEST))
                        .header(AUTHORIZATION, TEST_TOKEN_CORRECT))
                .andExpect(status().is(NOT_ACCEPTABLE.value()))
                .andExpect(jsonPath("$.code", is(GENRE_ERROR_TEST.code())))
                .andExpect(jsonPath("$.message", is(GENRE_ERROR_TEST.message())));
    }

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres PUT authorization works")
    void putMyGenresUnauthorized() throws Exception {
        // given
        Genre genre1 = genreFactory.createGenre("rock");
        Genre genre2 = genreFactory.createGenre("pop");
        doThrow(new AssertionError("Genres in body and genres given does not match"))
                .when(myGenresModifyPort)
                .putMyGenres(argThat(cmd ->
                        cmd.id().equals(TEST_ID) && !cmd.genres().equals(Set.of(genre1, genre2))));

        // when then
        mvc.perform(put("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .content(readAllBytes(CORRECT_REQUEST))
                        .header(AUTHORIZATION, TEST_TOKEN_INVALID))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(NOT_AUTHENTICATED.code())))
                .andExpect(jsonPath("$.message", is(NOT_AUTHENTICATED.message())));
    }

}
