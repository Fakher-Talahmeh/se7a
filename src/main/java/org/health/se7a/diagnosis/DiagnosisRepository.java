package org.health.se7a.diagnosis;

import org.health.se7a.common.XppResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Page<Diagnosis> findDiagnosesByPatient_NationalityID(String patientNationalityID, Pageable pageable);
    Page<Diagnosis> findDiagnosisByDoctor_Id(Long doctorId, Pageable pageable);
}
