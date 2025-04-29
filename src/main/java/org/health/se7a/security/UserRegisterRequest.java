package org.health.se7a.security;

import org.health.se7a.security.model.LoginType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public abstract class UserRegisterRequest {

    @NotNull(message = "{user.phoneNumber.notnull}")
    @Pattern(regexp = "^(\\+970|970|0)(59[245789]|56[26789])[0-9]{6}$", message = "{user.phoneNumber.invalid}")
    private String phoneNumber;

    @NotNull(message = "{user.name.notnull}")
    @Size(min = 3, message = "{user.name.size}")
    @Pattern(regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$", message = "{Invalid.name.format}")
    private String name;


    public abstract LoginType getType();

}