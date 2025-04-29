package org.health.se7a.security;

import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;

import java.util.Optional;

public interface UserRepository {

    Optional<? extends LoginUser> findByTelNumber(String telNumber);

    Optional<? extends LoginUser> findByName(String name);

    Optional<? extends LoginUser> loadById(Long id);

    LoginType getType();
}
