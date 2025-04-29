package org.health.se7a.statistics;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.health.se7a.patients.PatientRepository;
import org.health.se7a.patients.SmokingStatus;
import org.springframework.stereotype.Service;
import org.health.se7a.patients.Patients;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final PatientRepository patientsRepository;


    @Override
    @Cacheable(value = "statsCache", key = "#request.hashCode()")
    public StatisticsResponseDTO generateAdvancedStats(StatisticsRequestDTO request) {
        Specification<Patients> spec = buildSpecification(request);
        List<Patients> patients = fetchFilteredData(spec, request.isLargeDataset());

        StatisticsResponseDTO response = new StatisticsResponseDTO();
        calculateBasicStats(response, patients);

        if(request.isIncludeGenderAnalysis()) {
            response.setPatientsByGender(
                    patients.stream()
                            .collect(Collectors.groupingBy(
                                    Patients::getGender,
                                    Collectors.counting()
                            ))
            );
        }

        if(request.isIncludeSmokingAnalysis()) {
            response.setPatientsBySmokingStatus(
                    patients.stream()
                            .collect(Collectors.groupingBy(
                                    Patients::getSmokingStatus,
                                    Collectors.counting()
                            ))
            );
        }

        if(request.isIncludeDiseaseAnalysis()) {
            response.setPatientsByDisease(
                    patients.stream()
                            .flatMap(p -> p.getChronicDiseases().stream())
                            .collect(Collectors.groupingBy(
                                    Function.identity(),
                                    Collectors.counting()
                            ))
            );
        }

        if(request.isIncludeCombinedStats()) {
            calculateCombinedStats(response, patients);
        }

        return response;
    }

    private Specification<Patients> buildSpecification(StatisticsRequestDTO request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.getStartDate() != null && request.getEndDate() != null) {
                predicates.add(cb.between(
                        root.get("createdAt"),
                        request.getStartDate().atStartOfDay(),
                        request.getEndDate().atTime(LocalTime.MAX)
                ));
            }

            Optional.ofNullable(request.getGenderFilter())
                    .ifPresent(g -> predicates.add(cb.equal(root.get("gender"), g)));

            Optional.ofNullable(request.getSmokingFilter())
                    .ifPresent(s -> predicates.add(cb.equal(root.get("smokingStatus"), s)));

            Optional.ofNullable(request.getDiseaseFilter())
                    .ifPresent(d -> predicates.add(cb.isMember(d, root.get("chronicDiseases"))));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private List<Patients> fetchFilteredData(Specification<Patients> spec, boolean isLargeDataset) {
        if(isLargeDataset) {
            Page<Patients> page = patientsRepository.findAll(spec, PageRequest.of(0, 5000));
            return page.getContent();
        }
        return patientsRepository.findAll(spec);
    }

    private void calculateBasicStats(StatisticsResponseDTO response, List<Patients> patients) {
        response.setTotalPatients((long) patients.size());
        response.setTotalSmokers(patients.stream()
                .filter(p -> p.getSmokingStatus() == SmokingStatus.SMOKER)
                .count());
        response.setTotalWithChronicDiseases(patients.stream()
                .filter(p -> !p.getChronicDiseases().isEmpty())
                .count());
    }

    private void calculateCombinedStats(StatisticsResponseDTO response, List<Patients> patients) {
        // Gender-Smoking Combination
        response.setGenderSmokingCombination(
                patients.stream()
                        .collect(Collectors.groupingBy(
                                Patients::getGender,
                                Collectors.groupingBy(
                                        Patients::getSmokingStatus,
                                        Collectors.counting()
                                )
                        ))
        );

        response.setDiseaseGenderCombination(
                patients.stream()
                        .flatMap(p -> p.getChronicDiseases().stream()
                                .map(d -> Pair.of(d, p.getGender()))
                        )
                        .collect(Collectors.groupingBy(
                                Pair::getLeft,
                                Collectors.groupingBy(
                                        Pair::getRight,
                                        Collectors.counting()
                                )
                        ))
        );
    }

    @Override // إضافة annotation
    public ExportedPdfDTO exportStatisticsAsPdf(StatisticsRequestDTO dto) {
        StatisticsResponseDTO stats = generateAdvancedStats(dto);
        byte[] pdfBytes = PdfGenerator.generateAdvancedReport(stats, "/home/osama/Desktop/se7a/src/main/resources/static/images/logo.png");
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        ExportedPdfDTO response = new ExportedPdfDTO();
        response.setFileName("health-report.pdf");
        response.setContent(base64Pdf);
        return response;
    }
}