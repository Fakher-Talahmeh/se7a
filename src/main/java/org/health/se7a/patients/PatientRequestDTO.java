package org.health.se7a.patients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.health.se7a.common.OnCreate;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PatientRequestDTO {

    @NotBlank(message = "{doctor.name.notblank}",groups = OnCreate.class)
    @Size(min = 2, max = 100, message = "{doctor.name.size}",groups = OnCreate.class)
    private String name;

    @NotBlank(message = "{doctor.telNumber.notblank}",groups = OnCreate.class)
    @Pattern(regexp = "^(\\+970|970|0)(59[245789]|56[26789])[0-9]{6}$", message = "{doctor.telNumber.pattern}",groups = OnCreate.class)
    private String telNumber;

    @NotNull(message = "{patient.nationalityID.notNull}")
    private String nationalityID;

    @NotNull(message = "{patient.age.notNull}")
    private Long age;

    @NotNull(message = "{patient.gender.notNull}")
    private Gender gender;

    private List<ChronicDisease> chronicDiseases;
    private SmokingStatus smokingStatus;
}
