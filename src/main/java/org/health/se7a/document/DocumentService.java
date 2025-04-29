package org.health.se7a.document;

import org.health.se7a.exception.XppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
    Boolean addDocument(MedicalDocumentRequestDTO document);
    Boolean updateDocument(MedicalDocumentRequestDTO document) throws XppException;
    Boolean deleteDocument(Long id);
    MedicalDocumentResponseDTO getDocumentById(Long id);
    Page<MedicalDocumentResponseDTO> getDocumentByPatientNatId(String natId, Pageable page);
}
