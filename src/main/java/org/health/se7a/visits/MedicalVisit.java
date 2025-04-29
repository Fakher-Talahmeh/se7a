package org.health.se7a.visits;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.diagnosis.Diagnosis;
import org.health.se7a.doctor.Doctor;
import org.health.se7a.document.MedicalDocument;
import org.health.se7a.labtests.LabTest;
import org.health.se7a.medications.Medication;
import org.health.se7a.patients.Patients;
import org.health.se7a.vitalsigns.VitalSigns;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime visitDate;
    private String visitReason;
    private String notes;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patients patients;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor attendingPhysician;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<VitalSigns> vitalSigns;

    @OneToOne(cascade = CascadeType.ALL)
    private Diagnosis diagnosis;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<Medication> medications;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<LabTest> labTests;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<MedicalDocument> documents;

    private LocalDateTime visitTime;
}