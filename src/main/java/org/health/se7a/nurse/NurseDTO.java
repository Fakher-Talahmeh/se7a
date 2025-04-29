package org.health.se7a.nurse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;
import org.health.se7a.security.model.AccountStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NurseDTO {


    private Long id;

    @NotBlank(message = "{nurse.name.notblank}", groups = OnCreate.class)
    @Size(min = 2, max = 100, message = "{nurse.name.size}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "{nurse.telNumber.notblank}", groups = OnCreate.class)
    @Pattern(regexp = "^(\\+970|970|0)(59[245789]|56[26789])[0-9]{6}$", message = "{nurse.telNumber.pattern}", groups = {OnCreate.class, OnUpdate.class})
    private String telNumber;

}
