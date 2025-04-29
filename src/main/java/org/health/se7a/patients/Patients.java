package org.health.se7a.patients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.vitalsigns.VitalSigns;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Patients {

    @Id
    @GeneratedValue(generator = "IDGenerator")
    @GenericGenerator(name = "IDGenerator", strategy = "org.health.se7a.common.IDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String name;
    private String telNumber;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(unique = true, nullable = false)
    private String nationalityID;

    private Long age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany
    @JoinTable(
            name = "doctor_patients",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private List<Doctor> doctors;

    @ManyToMany
    @JoinTable(
            name = "nurse_patients",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "nurse_id")
    )
    @JsonManagedReference
    @JsonIgnore
    private List<Nurse> nurses;


    @ElementCollection(targetClass = ChronicDisease.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "patient_chronic_diseases",
            joinColumns = @JoinColumn(name = "patient_id")
    )
    @Column(name = "disease")
    private List<ChronicDisease> chronicDiseases;

    @Enumerated(EnumType.STRING)
    private SmokingStatus smokingStatus;
}
