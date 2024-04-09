package org.codebusters.audiogeek.preferencesagent.application.auth.token;

import org.codebusters.audiogeek.preferencesagent.shared.huelltoken.HuellToken;
import org.codebusters.audiogeek.preferencesagent.shared.huelltoken.HuellTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.codebusters.audiogeek.preferencesagent.shared.huelltoken.ErrorData.WEAK_SIGNATURE;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("HuellToken")
public class HuellTokenWeakKeyTest {

    @Autowired
    private HuellToken huellToken;

    
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("jwt.secret", () -> "N1y2rHlRiosNm9fiWgpArgj");
    }
    @Test
    @DisplayName("HuellToken - test if weak signature is detected")
    void testWeakSignatureToken() {
        // given
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIVUVMTCIsInN1YiI6IkhVRUxMIiwiaWF0IjoxNjk5NjQ5NzYzLCJ1c2VyX2lkIjoiMjc0ZmZmMDItODExOC00OTIxLWI3ODItZTI3NjVhZWQ4YjAzIn0.j97TWZmrakNaDTCcjxKROiGBJsSxsUrvAiLgxldTnMw";
        // when then
        assertThatThrownBy(() -> huellToken.parse(token)).isInstanceOf(HuellTokenException.class)
                .hasMessage(WEAK_SIGNATURE.message());
    }
}
