package org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class GenreFactoryConfig {
    @Bean
    GenreFactory genreFactory(@Value("${agent.genre.max-length}") Integer maxLength,
                              @Value("${agent.genre.char-whitelist}") String whitelist) {

        var validationProps = new GenreValidationProps(maxLength, whitelist);
        log.info("Initializing GenreFactory: {}", validationProps);
        return new GenreFactory(validationProps);
    }
}
