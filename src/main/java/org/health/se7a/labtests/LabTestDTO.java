package org.health.se7a.labtests;

import jakarta.validation.constraints.*;
import lombok.*;
import org.health.se7a.common.OnCreate;
import org.health.se7a.nurse.NurseDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabTestDTO {

    private Long id;

    @NotNull(message = "{labtest.patientNatId.notnull}", groups = OnCreate.class)
    private String patientNatId;

    @NotNull(message = "{labtest.testName.notnull}", groups = OnCreate.class)
    @Size(min = 2, max = 100, message = "{labtest.testName.size}", groups = OnCreate.class)
    private String testName;

    @NotNull(message = "{labtest.result.notnull}", groups = OnCreate.class)
    @Size(min = 1, max = 500, message = "{labtest.result.size}", groups = OnCreate.class)
    private String result;

    @NotNull(message = "{labtest.testDate.notnull}", groups = OnCreate.class)
    private LocalDateTime testDate;




}
