package org.health.se7a.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {

    private Long id;

    @NotBlank(message = "{doctor.name.notblank}",groups = OnCreate.class)
    @Size(min = 2, max = 100, message = "{doctor.name.size}",groups = OnCreate.class)
    private String name;

    @NotBlank(message = "{doctor.telNumber.notblank}",groups = OnCreate.class)
    @Pattern(regexp = "^(\\+970|970|0)(59[245789]|56[26789])[0-9]{6}$", message = "{doctor.telNumber.pattern}",groups = OnCreate.class)
    private String telNumber;

    @NotBlank(message = "{doctor.specialty.notblank}",groups = OnCreate.class)
    private String specialty;

    @NotBlank(message = "{doctor.licenceNumber.notblank}",groups = OnCreate.class)
    @Size(min = 5, max = 20, message = "{doctor.licenceNumber.size}",groups = OnCreate.class)
    private String licenceNumber;

}
