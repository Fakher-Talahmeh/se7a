package org.health.se7a.statistics;

import lombok.RequiredArgsConstructor;
import org.health.se7a.common.XppResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    @PreAuthorize("@authorizationService.loggedInUserIsSecretary()")
    public XppResponseEntity<StatisticsResponseDTO> getStats(@RequestBody @Validated StatisticsRequestDTO dto) {
        StatisticsResponseDTO stats = statisticsService.generateAdvancedStats(dto);
        return XppResponseEntity.map(stats);
    }

    @PostMapping("/export-pdf")
    @PreAuthorize("@authorizationService.loggedInUserIsSecretary()")
    public XppResponseEntity<ExportedPdfDTO> exportStatsPdf(@RequestBody @Validated StatisticsRequestDTO dto) {
        ExportedPdfDTO base64Pdf = statisticsService.exportStatisticsAsPdf(dto);
        return XppResponseEntity.map(base64Pdf);
    }
}