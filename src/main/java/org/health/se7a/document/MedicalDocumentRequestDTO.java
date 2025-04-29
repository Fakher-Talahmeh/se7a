package org.health.se7a.document;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.health.se7a.common.OnCreate;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalDocumentRequestDTO {
    private Long id;

    @NotNull(message = "{document.file.notnull}", groups = OnCreate.class)
    private MultipartFile file;

    @NotNull(message = "{document.patientId.notnull}", groups = OnCreate.class)
    private String patientNatId;
}
