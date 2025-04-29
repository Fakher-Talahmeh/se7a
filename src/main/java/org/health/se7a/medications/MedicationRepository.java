package org.health.se7a.medications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    long countByAdministeredAtBetween(LocalDateTime start, LocalDateTime end);

    Page<Medication> findByPatient_NationalityID(String patientNationalityID, Pageable pageable);
    Page<Medication> findMedicationsByNurseId(Long nurseID, Pageable pageable);
}
