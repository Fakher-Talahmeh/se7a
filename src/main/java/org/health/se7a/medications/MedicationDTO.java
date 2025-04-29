package org.health.se7a.medications;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Valid
public class MedicationDTO {

    private Long id;

    @NotNull(message = "{medication.patientNatId.notnull}", groups = OnCreate.class)
    private String patientNatId;

    @NotNull(message = "{medication.drugName.notnull}", groups = {OnCreate.class})
    @Size(min = 2, max = 100, message = "{medication.drugName.size}", groups = {OnCreate.class, OnUpdate.class})
    private String drugName;

    @NotNull(message = "{medication.dosage.notnull}", groups = {OnCreate.class})
    @Positive(message = "{medication.dosage.positive}", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "0.1", message = "{medication.dosage.min}", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "1000.0", message = "{medication.dosage.max}", groups = {OnCreate.class, OnUpdate.class})
    private Double dosage;

    @NotNull(message = "{medication.administrationMethod.notnull}", groups = {OnCreate.class})
    @Size(min = 2, max = 50, message = "{medication.administrationMethod.size}", groups = {OnCreate.class, OnUpdate.class})
    private String administrationMethod;

    @NotNull(message = "{medication.administeredAt.notnull}", groups = OnCreate.class)
    private LocalDateTime administeredAt;
}
