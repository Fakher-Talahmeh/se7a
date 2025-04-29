package org.health.se7a.admin;

import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, UserRepository {
    @Override
    default LoginType getType() {
        return LoginType.ADMIN;
    }

    @Override
    default Optional<Admin> loadById(Long id) {
        return findById(id);
    }

    Boolean existsByName(String name);

    Boolean existsByTelNumber(String telNumber);



}
