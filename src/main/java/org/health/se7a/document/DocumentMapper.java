package org.health.se7a.document;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DocumentMapper {

    public static MedicalDocumentResponseDTO toDto(MedicalDocument document) {
        return Optional.ofNullable(document)
                .map(d -> MedicalDocumentResponseDTO.builder()
                        .id(d.getId())
                        .fileName(d.getFileName())
                        .fileType(d.getFileType())
                        .fileSize(d.getFileSize())
                        .uploadedAt(d.getUploadedAt())
                        .fileData(d.getData())
                        .patientId(d.getPatient() != null ? d.getPatient().getId() : null)
                        .build())
                .orElse(null);
    }

    public static MedicalDocument toEntity(MedicalDocumentResponseDTO dto) {
        return Optional.ofNullable(dto)
                .map(d -> MedicalDocument.builder()
                        .id(d.getId())
                        .fileName(d.getFileName())
                        .fileType(d.getFileType())
                        .fileSize(d.getFileSize())
                        .uploadedAt(d.getUploadedAt())
                        .build())
                .orElse(null);
    }
    public static List<MedicalDocumentResponseDTO> toDtoList(List<MedicalDocument> list) {
        return list == null ? null : list.stream()
                .map(DocumentMapper::toDto)
                .collect(Collectors.toList());
    }
}
