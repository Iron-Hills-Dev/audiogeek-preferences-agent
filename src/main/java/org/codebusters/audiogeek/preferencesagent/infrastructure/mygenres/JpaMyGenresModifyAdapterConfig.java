package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.GenreRepository;
import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class JpaMyGenresModifyAdapterConfig {
    @Bean
    JpaMyGenresModifyAdapter jpaMyGenresModifyAdapter(UserRepository userRepo, GenreRepository genreRepo) {
        log.info("Initializing JpaMyGenresModifyAdapter");
        return new JpaMyGenresModifyAdapter(userRepo, genreRepo);
    }
}
