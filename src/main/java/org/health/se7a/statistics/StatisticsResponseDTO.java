package org.health.se7a.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.health.se7a.patients.ChronicDisease;
import org.health.se7a.patients.Gender;
import org.health.se7a.patients.SmokingStatus;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsResponseDTO {
    // Basic Stats
    private Long totalPatients;
    private Long totalSmokers;
    private Long totalWithChronicDiseases;

    // Detailed Analysis
    private Map<Gender, Long> patientsByGender;
    private Map<SmokingStatus, Long> patientsBySmokingStatus;
    private Map<ChronicDisease, Long> patientsByDisease;

    // Combined Stats
    private Map<Gender, Map<SmokingStatus, Long>> genderSmokingCombination;
    private Map<ChronicDisease, Map<Gender, Long>> diseaseGenderCombination;
}