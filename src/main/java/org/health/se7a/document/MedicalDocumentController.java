package org.health.se7a.document;

import lombok.RequiredArgsConstructor;
import org.health.se7a.common.OnCreate;
import org.health.se7a.common.XppResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class MedicalDocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@authorizationService.loggedInUserIsSecretary()")
    public XppResponseEntity<Boolean> uploadDocument(
            @Validated(OnCreate.class) MedicalDocumentRequestDTO requestDTO
    ) {
        return XppResponseEntity.map(documentService.addDocument(requestDTO));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@authorizationService.loggedInUserIsSecretary()")
    public XppResponseEntity<Boolean> updateDocument(
            @RequestParam Long id,
            @Validated(OnCreate.class) MedicalDocumentRequestDTO requestDTO
    ) {
        requestDTO.setId(id);
        return XppResponseEntity.map(documentService.updateDocument(requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.loggedInUserIsSecretary()")
    public XppResponseEntity<Boolean> deleteDocument(
            @PathVariable Long id
    ) {
        return XppResponseEntity.map(documentService.deleteDocument(id));
    }

    @GetMapping("/{id}")
    public XppResponseEntity<MedicalDocumentResponseDTO> getDocumentById(@PathVariable Long id) {
        return XppResponseEntity.map(documentService.getDocumentById(id));
    }

    @GetMapping("/by-patient/{patientNatId}")
    public XppResponseEntity<Page<MedicalDocumentResponseDTO>> getDocumentsByPatientNatId(
            Pageable pageable,
            @PathVariable String patientNatId
    ) {
        return XppResponseEntity.map(documentService.getDocumentByPatientNatId(patientNatId, pageable));
    }
}
