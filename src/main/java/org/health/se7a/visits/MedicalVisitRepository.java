package org.health.se7a.visits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
    Page<MedicalVisit> getMedicalVisitByPatients_NationalityID(String natId, Pageable page);

    @Query("SELECT mv FROM MedicalVisit mv " +
            "WHERE mv.attendingPhysician.id = :doctorId " +
            "AND mv.visitTime BETWEEN :startOfDay AND :endOfDay")
    Page<MedicalVisit> findVisitsByDoctorAndDayRange(
            @Param("doctorId") Long doctorId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable);

}
