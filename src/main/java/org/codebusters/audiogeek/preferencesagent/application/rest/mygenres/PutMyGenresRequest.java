package org.codebusters.audiogeek.preferencesagent.application.rest.mygenres;

import java.util.Set;

record PutMyGenresRequest(Set<String> genres) {
}
