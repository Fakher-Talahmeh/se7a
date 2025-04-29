package org.health.se7a.visits;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.health.se7a.common.OnCreate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalVisitRequestDTO {

    private Long id;
    @NotNull(message = "{medicalvisit.visitReason.notnull}", groups = OnCreate.class)
    @Size(min = 2, max = 255, message = "{medicalvisit.visitReason.size}", groups = OnCreate.class)
    private String visitReason;

    @NotNull(message = "{medicalvisit.notes.notnull}", groups = OnCreate.class)
    @Size(min = 2, max = 500, message = "{medicalvisit.notes.size}", groups = OnCreate.class)
    private String notes;

    @NotNull(message = "{medicalvisit.visitDate.notnull}", groups = OnCreate.class)
    private String visitDate;

    @NotNull(message = "{medicalvisit.doctorId.notnull}", groups = OnCreate.class)
    private Long doctorId;

    @NotNull(message = "{medicalvisit.patientNatId.notnull}", groups = OnCreate.class)
    private String patientNatId;
}
