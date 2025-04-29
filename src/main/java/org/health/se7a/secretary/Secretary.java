package org.health.se7a.secretary;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import org.health.se7a.users.User;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class Secretary extends User implements LoginUser {

    @Override
    public LoginType getType() {
        return LoginType.SECRETARY;
    }
}
