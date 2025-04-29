package org.health.se7a.statistics;


public interface StatisticsService {
    StatisticsResponseDTO generateAdvancedStats(StatisticsRequestDTO dto);
    ExportedPdfDTO exportStatisticsAsPdf(StatisticsRequestDTO dto);
}