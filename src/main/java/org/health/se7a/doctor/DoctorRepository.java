package org.health.se7a.doctor;

import org.health.se7a.security.UserRepository;
import org.health.se7a.security.model.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> , UserRepository {
    @Override
    default LoginType getType() {
        return LoginType.DOCTOR;
    }

    @Override
    default Optional<Doctor> loadById(Long id) {
        return findById(id);
    }

    Boolean existsByTelNumber(String telNumber);
}
