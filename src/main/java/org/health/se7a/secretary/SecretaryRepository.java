package org.health.se7a.secretary;

import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, Long>, UserRepository {
    @Override
    default LoginType getType() {
        return LoginType.SECRETARY;
    }

    @Override
    default Optional<Secretary> loadById(Long id) {
        return findById(id);
    }
}
