package org.health.se7a.diagnosis;

import jakarta.validation.constraints.*;
import lombok.*;
import org.health.se7a.common.OnCreate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiagnosisDTO {

    private Long id;

    @NotNull(message = "{diagnosis.patientNatId.notnull}", groups = OnCreate.class)
    private String patientNatId;

    @NotNull(message = "{diagnosis.details.notnull}", groups = OnCreate.class)
    @Size(min = 2, max = 1000, message = "{diagnosis.details.size}", groups = OnCreate.class)
    private String diagnosisDetails;

    @Size(max = 1000, message = "{diagnosis.notes.size}", groups = OnCreate.class)
    private String notes;

    private LocalDateTime recordedAt;

}
