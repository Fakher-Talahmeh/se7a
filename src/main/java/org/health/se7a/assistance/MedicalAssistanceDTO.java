package org.health.se7a.assistance;

import jakarta.validation.constraints.*;
import lombok.*;
import org.health.se7a.common.OnCreate;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalAssistanceDTO {

    private Long id;


    @NotNull(message = "{assistance.doctorId.notnull}", groups = OnCreate.class)
    private Long doctorId;

    @NotNull(message = "{assistance.patientNatId.notnull}", groups = OnCreate.class)
    private String patientNatId;

    @Size(max = 500, message = "{assistance.notes.size}", groups = OnCreate.class)
    private String notes;

    private List<@Size(min = 1, max = 100, message = "{assistance.tool.size}") String> toolsPrepared;
}
