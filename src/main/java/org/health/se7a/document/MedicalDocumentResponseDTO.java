package org.health.se7a.document;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDocumentResponseDTO {

    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Long patientId;
    private LocalDateTime uploadedAt;
    private byte[] fileData;
}