package org.health.se7a.diagnosis;
import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.patients.Patients;
import org.health.se7a.visits.MedicalVisit;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private String diagnosisDetails;
    private String notes;
    private LocalDateTime recordedAt;


    @ManyToOne
    private MedicalVisit visit;
}