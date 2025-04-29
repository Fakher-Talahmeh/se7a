package org.health.se7a.labtests;

import org.health.se7a.medications.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    Page<LabTest> findLabTestByPatient_NationalityID(String natId, Pageable pageable);
    Page<LabTest> findLabTestByNurse_Id(Long nurseId, Pageable pageable);
    long countByTestDateBetween(LocalDateTime start, LocalDateTime end);

}
