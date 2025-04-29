package org.health.se7a.admin;

import lombok.experimental.SuperBuilder;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.users.User;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class Admin extends User implements LoginUser {

    @Override
    public LoginType getType() {
        return LoginType.ADMIN;
    }

}



