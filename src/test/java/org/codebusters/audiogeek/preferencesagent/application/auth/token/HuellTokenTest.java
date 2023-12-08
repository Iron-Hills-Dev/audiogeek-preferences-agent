package org.codebusters.audiogeek.preferencesagent.application.auth.token;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.codebusters.audiogeek.preferencesagent.application.auth.token.ErrorData.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("HuellToken")
public class HuellTokenTest {

    @Autowired
    private HuellToken huellToken;

    // TODO remove after domain implementation
    @MockBean
    private MyGenresQueryPort myGenresQueryPort;

    @Test
    @DisplayName("HuellToken - test if token parses correctly")
    void testCorrectToken() {
        // given
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIVUVMTCIsInN1YiI6IkhVRUxMIiwiaWF0IjoxNjk5NjQ5NzYzLCJ1c2VyX2lkIjoiMjc0ZmZmMDItODExOC00OTIxLWI3ODItZTI3NjVhZWQ4YjAzIn0.GDvOyMPft_R18aZOtiG0pd4DNC6Wju58RAJWx3wY-aQ";
        // when then
        var payload = huellToken.parse(token);
        assertThat(payload.id()).isEqualTo(UUID.fromString("274fff02-8118-4921-b782-e2765aed8b03"));
    }

    @Test
    @DisplayName("HuellToken - test if wrong signature is detected")
    void testWrongSignatureToken() {
        // given
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIVUVMTCIsInN1YiI6IkhVRUxMIiwiaWF0IjoxNjk5NjQ5NzYzLCJ1c2VyX2lkIjoiMjc0ZmZmMDItODExOC00OTIxLWI3ODItZTI3NjVhZWQ4YjAzIn0.0gPg0D2hohepHi8J6fXkzpGMlVZpPzSrqKIrWvR0Hnk";
        // when then
        assertThatThrownBy(() -> huellToken.parse(token)).isInstanceOf(HuellTokenException.class)
                .hasMessage(WRONG_SIGNATURE.message());
    }

    @Test
    @DisplayName("HuellToken - test if wrong payload is detected")
    void testWrongPayloadToken() {
        // given
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIVUVMTCIsInN1YiI6IkhVRUxMIiwiaWF0IjoxNjk5NjQ5NzYzLCJ1c2VyX2lkIjoiaWRpZGlkIn0.450BQBV0YQQyNCwjNVv9-0_bheXXbpz1TtcuUKz3cMY";
        // when then
        assertThatThrownBy(() -> huellToken.parse(token)).isInstanceOf(HuellTokenException.class)
                .hasMessage(WRONG_PAYLOAD.message());
    }

    @Test
    @DisplayName("HuellToken - test if expired signature is detected")
    void testExpiredToken() {
        // given
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIVUVMTCIsInN1YiI6IkhVRUxMIiwiZXhwIjoxNjk5NjQxNTYzLCJpYXQiOjE2OTk2NDk3NjMsInVzZXJfaWQiOiIyNzRmZmYwMi04MTE4LTQ5MjEtYjc4Mi1lMjc2NWFlZDhiMDMifQ.l0-ZQDeenL7SAdI1L_jpkFyc81qjNmux4oyNTW_KAT8";
        // when then
        assertThatThrownBy(() -> huellToken.parse(token)).isInstanceOf(HuellTokenException.class)
                .hasMessageMatching(EXPIRED_TOKEN.message().replace("%s", "[A-Za-z0-9]+"));
    }
}
