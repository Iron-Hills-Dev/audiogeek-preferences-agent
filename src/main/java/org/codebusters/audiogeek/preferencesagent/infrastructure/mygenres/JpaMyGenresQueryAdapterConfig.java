package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.GenreFactory;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JpaMyGenresQueryAdapterConfig {

    @Bean
    JpaMyGenresQueryAdapter jpaMyGenresQueryAdapter(UserRepository userRepo, GenreFactory genreFactory) {
        log.info("Initializing JpaMyGenresQueryAdapter");
        return new JpaMyGenresQueryAdapter(userRepo, genreFactory);
    }
}
