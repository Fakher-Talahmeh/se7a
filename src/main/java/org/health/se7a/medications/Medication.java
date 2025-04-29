package org.health.se7a.medications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.patients.Patients;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.visits.MedicalVisit;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    private String drugName;
    private Double dosage;
    private String administrationMethod;
    private LocalDateTime administeredAt;

    @ManyToOne
    @JoinColumn(name = "nurse_id", nullable = false)
    private Nurse nurse;


    @ManyToOne
    @JsonIgnore
    private MedicalVisit visit;
}
