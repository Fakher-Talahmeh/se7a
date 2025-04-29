package org.health.se7a.prescription;

import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.patients.Patients;
import org.health.se7a.visits.MedicalVisit;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String medicationName;

    private String dosage;
    private String duration;
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "visit_id", nullable = false)
    private MedicalVisit visit;
}
