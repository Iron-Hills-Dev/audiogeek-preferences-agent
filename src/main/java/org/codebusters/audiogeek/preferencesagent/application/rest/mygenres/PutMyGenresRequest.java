package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import java.util.Set;

public record PutMyGenresRequest(Set<String> genres) {
}
