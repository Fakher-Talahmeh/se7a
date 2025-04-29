package org.health.se7a.statistics;

import lombok.Data;
import org.health.se7a.patients.ChronicDisease;
import org.health.se7a.patients.Gender;
import org.health.se7a.patients.SmokingStatus;

import java.time.LocalDate;

@Data
public class StatisticsRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;

    private Gender genderFilter;
    private SmokingStatus smokingFilter;
    private ChronicDisease diseaseFilter;

    private boolean includeGenderAnalysis;
    private boolean includeSmokingAnalysis;
    private boolean includeDiseaseAnalysis;
    private boolean includeCombinedStats;
    private boolean largeDataset;
}