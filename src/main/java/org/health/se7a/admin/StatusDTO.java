package org.health.se7a.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;

@Data
public class StatusDTO {

    @NotNull(message = "{phone.number.notnull}")
    private String phoneNumber;

    @NotNull(message = "{login.type.notnull}")
    private LoginType type;

    @NotNull(message = "{status.notnull}")
    private AccountStatus status;


}
