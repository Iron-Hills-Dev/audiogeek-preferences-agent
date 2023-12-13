package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.exception.UserNotFoundException;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

import static java.nio.file.Files.readString;
import static org.codebusters.audiogeek.preferencesagent.application.exception.ApiErrorData.NOT_AUTHENTICATED;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("MyGenresRestAdapter - /my-genres GET")
class MyGenresRestAdapterGetTest {
    private static final String PATH_PREFIX = "src/test/resources/application/rest/mygenres/";

    static final UserID TEST_ID = new UserID(UUID.fromString("274fff02-8118-4921-b782-e2765aed8b03"));
    static final String TEST_TOKEN_CORRECT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIVUVMTCIsInN1YiI6IkhVRUxMIiwiaWF0IjoxNjk5NjQ5NzYzLCJ1c2VyX2lkIjoiMjc0ZmZmMDItODExOC00OTIxLWI3ODItZTI3NjVhZWQ4YjAzIn0.GDvOyMPft_R18aZOtiG0pd4DNC6Wju58RAJWx3wY-aQ";
    static final String TEST_TOKEN_INVALID = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIVUVMTCIsInN1YiI6IkhVRUxMIiwiaWF0IjoxNjk5NjQ5NzYzLCJ1c2VyX2lkIjoiMjc0ZmZmMDItODExOC00OTIxLWI3ODItZTI3NjVhZWQ4YjAzIn0.fCYLVPg1dSwqjpvNOaYae95AHMYwfW7IRAVbDl4qnqY";

    private static final Path CORRECT_RESPONSE = Path.of(PATH_PREFIX + "get_response_correct.json");
    private static final Path EMPTY_RESPONSE = Path.of(PATH_PREFIX + "get_response_empty.json");


    @Autowired
    private GenreFactory genreFactory;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MyGenresQueryPort myGenresQueryPort;

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres GET works correctly")
    void getMyGenresCorrect() throws Exception {
        // given
        doReturn(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .when(myGenresQueryPort).getMyGenres(TEST_ID);

        // when then
        mvc.perform(get("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, TEST_TOKEN_CORRECT))
                .andExpect(status().isOk())
                .andExpect(content().json(readString(CORRECT_RESPONSE)));
    }

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres GET works correctly when genres are empty")
    void getMyGenresEmpty() throws Exception {
        // given
        doReturn(Set.of()).when(myGenresQueryPort).getMyGenres(TEST_ID);

        // when then
        mvc.perform(get("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, TEST_TOKEN_CORRECT))
                .andExpect(status().isOk())
                .andExpect(content().json(readString(EMPTY_RESPONSE)));
    }

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres GET works correctly when user to not exists")
    void getMyGenresUserDoNotExists() throws Exception {
        // given
        doThrow(UserNotFoundException.class).when(myGenresQueryPort).getMyGenres(TEST_ID);
        var expected = new UserNotFoundException();

        // when then
        mvc.perform(get("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, TEST_TOKEN_CORRECT))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(expected.code)))
                .andExpect(jsonPath("$.message", is(expected.message)));
    }

    @Test
    @DisplayName("MyGenresRestAdapter - test if /my-genres GET authorization works")
    void getMyGenresNotAuthorized() throws Exception {
        // given
        doReturn(Set.of(genreFactory.createGenre("rock"), genreFactory.createGenre("pop")))
                .when(myGenresQueryPort).getMyGenres(TEST_ID);

        // when then
        mvc.perform(get("/api/v1/my-genres")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, TEST_TOKEN_INVALID))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(NOT_AUTHENTICATED.code())))
                .andExpect(jsonPath("$.message", is(NOT_AUTHENTICATED.message())));
    }
}