package org.health.se7a.patients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientRepository extends
        JpaRepository<Patients, Long>,
        JpaSpecificationExecutor<Patients> {

    Optional<Patients> getPatientsByNationalityID(String id);
}