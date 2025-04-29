package org.health.se7a.document;

import jakarta.persistence.*;
import lombok.*;
import org.health.se7a.patients.Patients;
import org.health.se7a.visits.MedicalVisit;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private Long fileSize;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] data;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patients patient;

    private LocalDateTime uploadedAt;

    @ManyToOne
    private MedicalVisit visit;
}
