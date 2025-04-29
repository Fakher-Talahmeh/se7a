package org.health.se7a.nurse;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.health.se7a.patients.Patients;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import org.health.se7a.users.User;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Nurse extends User implements LoginUser {

    @ManyToMany(mappedBy = "nurses")
    @JsonBackReference
    private List<Patients> patients;

    @Override
    public LoginType getType() {
        return LoginType.NURSE;
    }
}

