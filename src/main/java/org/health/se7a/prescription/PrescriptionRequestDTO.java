package org.health.se7a.prescription;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequestDTO {

    private Long id;
    @NotBlank(message = "{prescription.medicationName.notBlank}")
    private String medicationName;

    @Size(max = 255, message = "{prescription.dosage.size}")
    private String dosage;

    @Size(max = 255, message = "{prescription.duration.size}")
    private String duration;

    @Size(max = 1000, message = "{prescription.instructions.size}")
    private String instructions;


    private String patientNatId;
}
