package org.health.se7a.assistance;

import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.nurse.Nurse;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.patients.Patients;

import java.util.List;

@Entity
@Table(name = "medical_assistances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalAssistance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nurse_id", nullable = false)
    private Nurse nurse;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;


    @ElementCollection
    @CollectionTable(name = "medical_assistance_tools", joinColumns = @JoinColumn(name = "medical_assistance_id"))
    @Column(name = "tool")
    private List<String> toolsPrepared;

    @Column(length = 500)
    private String notes;
}
