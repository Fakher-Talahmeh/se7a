package org.health.se7a.secretary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.health.se7a.common.OnCreate;
import org.health.se7a.common.OnUpdate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecretaryDTO {
    @NotNull(message = "{secretary.id.notnull}")
    private Long id;

    @NotBlank(message = "{secretary.name.notblank}", groups = OnCreate.class)
    @Size(min = 2, max = 100, message = "{secretary.name.size}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "{secretary.telNumber.notblank}", groups = OnCreate.class)
    @Pattern(regexp = "^(\\+970|970|0)(59[245789]|56[26789])[0-9]{6}$", message = "{secretary.telNumber.pattern}", groups = {OnCreate.class, OnUpdate.class})
    private String telNumber;

}
