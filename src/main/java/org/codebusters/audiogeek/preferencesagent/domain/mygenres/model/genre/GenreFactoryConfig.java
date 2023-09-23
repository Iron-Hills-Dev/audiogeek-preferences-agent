package org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GenreFactoryConfig {
    @Bean
    GenreFactory genreFactory() {
        return new GenreFactory();
    }
}
