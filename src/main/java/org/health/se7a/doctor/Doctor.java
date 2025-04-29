package org.health.se7a.doctor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.health.se7a.common.BaseEntity;
import org.health.se7a.patients.Patients;
import org.health.se7a.security.model.AccountStatus;
import org.health.se7a.security.model.LoginType;
import org.health.se7a.security.model.LoginUser;
import org.health.se7a.users.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity

public class Doctor extends User implements LoginUser {


    private String specialty;



    @ManyToMany(mappedBy = "doctors")
    private List<Patients> patients;

    @Column(unique = true,nullable = false)
    private String licenceNumber;

    @Override
    public LoginType getType() {
        return LoginType.DOCTOR;
    }

}
