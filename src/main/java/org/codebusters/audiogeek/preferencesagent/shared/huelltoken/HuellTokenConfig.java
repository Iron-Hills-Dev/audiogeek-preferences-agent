package org.codebusters.audiogeek.preferencesagent.shared.huelltoken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HuellTokenConfig {
    @Bean
    HuellToken huellToken(@Value("${jwt.secret}") String secret, @Value("${jwt.algorithm}") String algorithm) {
        return new HuellToken(secret, algorithm);
    }
}
