package org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.repo;

import org.codebusters.audiogeek.preferencesagent.infrastructure.mygenres.db.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
