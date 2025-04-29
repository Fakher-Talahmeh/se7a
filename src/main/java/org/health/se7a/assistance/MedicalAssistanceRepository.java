package org.health.se7a.assistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalAssistanceRepository extends JpaRepository<MedicalAssistance, Long> {

    Optional<MedicalAssistance> findById(Long id);
    Page<MedicalAssistance> getAllByDoctor_Id(Long doctorId,Pageable pageable);
    Page<MedicalAssistance> getAllByNurse_Id(Long nurseId,Pageable pageable);
    Page<MedicalAssistance> findMedicalAssistanceByPatient_NationalityID(String natId, Pageable pageable);

}
