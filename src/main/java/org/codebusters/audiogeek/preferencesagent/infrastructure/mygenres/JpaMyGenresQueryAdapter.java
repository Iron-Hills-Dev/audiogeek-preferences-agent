package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.MyGenresQueryPort;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;

import java.util.Set;

public class JpaMyGenresQueryAdapter implements MyGenresQueryPort {
    @Override
    public Set<Genre> getMyGenres(UserID id) {
        return null;
    }
}
