package org.codebusters.audiogeek.preferencesagent.domain.mygenres;

import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.genre.Genre;
import org.codebusters.audiogeek.preferencesagent.domain.mygenres.model.UserID;

import java.util.Set;

public interface GetMyGenresPort {
    Set<Genre> getMyGenres(UserID id);
}
