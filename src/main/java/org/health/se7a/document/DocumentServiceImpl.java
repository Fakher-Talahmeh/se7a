package org.health.se7a.document;

import lombok.RequiredArgsConstructor;
import org.health.se7a.exception.XppException;
import org.health.se7a.patients.PatientService;
import org.health.se7a.patients.Patients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final MedicalDocumentRepository documentRepository;
    private final PatientService patientService;

    @Override
    @Transactional
    public Boolean addDocument(MedicalDocumentRequestDTO requestDTO) {
        Patients patient = patientService.getPatientByNatId(requestDTO.getPatientNatId());
        try {
            MedicalDocument document = MedicalDocument.builder()
                    .fileName(requestDTO.getFile().getOriginalFilename())
                    .fileType(requestDTO.getFile().getContentType())
                    .fileSize(requestDTO.getFile().getSize())
                    .data(requestDTO.getFile().getBytes())
                    .patient(patient)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            documentRepository.save(document);
            return true;
        } catch (IOException e) {
            throw new XppException(List.of(requestDTO.getFile().getOriginalFilename()), HttpStatus.INTERNAL_SERVER_ERROR, "document.upload.failed");
        }
    }

    @Override
    @Transactional
    public Boolean updateDocument(MedicalDocumentRequestDTO requestDTO) {
        MedicalDocument document = documentRepository.findById(requestDTO.getId())
                .orElseThrow(() -> notFoundException(requestDTO.getId(), "document.notfound"));

        try {
            document.setFileType(requestDTO.getFile().getContentType());
            document.setFileSize(requestDTO.getFile().getSize());
            document.setData(requestDTO.getFile().getBytes());
            document.setUploadedAt(LocalDateTime.now());

            documentRepository.save(document);
            return true;

        } catch (IOException e) {
            throw new XppException(
                    List.of(requestDTO.getFile().getOriginalFilename()),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "document.update.failed"
            );
        }
    }


    @Override
    @Transactional
    public Boolean deleteDocument(Long id) {
        MedicalDocument document = documentRepository.findById(id)
                .orElseThrow(() -> notFoundException(id, "document.notfound"));

        documentRepository.delete(document);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalDocumentResponseDTO getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(DocumentMapper::toDto)
                .orElseThrow(() -> notFoundException(id, "document.notfound"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalDocumentResponseDTO> getDocumentByPatientNatId(String natId, Pageable pageable) {
        Patients patient = patientService.getPatientByNatId(natId);
        return documentRepository.findByPatientId(patient.getId(), pageable)
                .map(DocumentMapper::toDto);
    }

    private XppException notFoundException(Object identifier, String messageKey) {
        return new XppException(
                List.of(identifier),
                HttpStatus.NOT_FOUND,
                messageKey
        );
    }
}
