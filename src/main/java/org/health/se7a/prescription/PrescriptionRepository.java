package org.health.se7a.prescription;

import org.health.se7a.common.XppResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Page<Prescription> findPrescriptionByVisit_Id(Long visitId, Pageable pageable);
    Page<Prescription> findPrescriptionByPatient_NationalityID(String patientNatId, Pageable pageable);
    Page<Prescription> findPrescriptionByDoctor_Id(Long id, Pageable pageable);

}
