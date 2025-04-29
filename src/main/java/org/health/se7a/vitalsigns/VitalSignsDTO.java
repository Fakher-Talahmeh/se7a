package org.health.se7a.vitalsigns;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Valid
public class VitalSignsDTO {
    private Long id;

    @NotNull(message = "{vitals.patient.notnull}", groups = OnCreate.class)
    private String patientNatId;

    @Positive(message = "{vitals.bloodPressure.positive}", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "40.0", message = "{vitals.bloodPressure.min}", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "200.0", message = "{vitals.bloodPressure.max}", groups = {OnCreate.class, OnUpdate.class})
    private Double bloodPressure;

    @Positive(message = "{vitals.heartRate.positive}", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 30, message = "{vitals.heartRate.min}", groups = {OnCreate.class, OnUpdate.class})
    @Max(value = 200, message = "{vitals.heartRate.max}", groups = {OnCreate.class, OnUpdate.class})
    private Integer heartRate;

    @Positive(message = "{vitals.temperature.positive}", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "35.0", message = "{vitals.temperature.min}", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "42.0", message = "{vitals.temperature.max}", groups = {OnCreate.class, OnUpdate.class})
    private Double temperature;

    @Positive(message = "{vitals.respiratoryRate.positive}", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 10, message = "{vitals.respiratoryRate.min}", groups = {OnCreate.class, OnUpdate.class})
    @Max(value = 40, message = "{vitals.respiratoryRate.max}", groups = {OnCreate.class, OnUpdate.class})
    private Integer respiratoryRate;

    @NotNull(message = "{vitals.recordedAt.notnull}", groups = OnCreate.class)
    private LocalDateTime recordedAt;
}
