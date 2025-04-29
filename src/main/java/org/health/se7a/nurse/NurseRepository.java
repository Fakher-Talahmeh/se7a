package org.health.se7a.nurse;

import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> , UserRepository {

    @Override
    default LoginType getType() {
        return LoginType.NURSE;
    }

    @Override
    default Optional<Nurse> loadById(Long id) {
        return findById(id);
    }


}
