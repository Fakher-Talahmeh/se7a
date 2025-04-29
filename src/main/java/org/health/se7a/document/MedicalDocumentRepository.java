package org.health.se7a.document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Integer> {

    Optional<MedicalDocument> findByFileName(String name);
    Optional<MedicalDocument> findById(Long documentId);
    Page<MedicalDocument> findByPatientId(Long patientId, Pageable pageable);
}
