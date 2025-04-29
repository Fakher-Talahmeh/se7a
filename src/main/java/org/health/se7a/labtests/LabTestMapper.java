package org.health.se7a.labtests;

import org.health.se7a.nurse.NurseMapper;
import org.health.se7a.patients.PatientMapper;
import org.health.se7a.patients.Patients;
import org.health.se7a.vitalsigns.VitalSigns;
import org.health.se7a.vitalsigns.VitalSignsMapper;
import org.health.se7a.vitalsigns.VitalSignsResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LabTestMapper {

    public static LabTestResponseDTO toResponse(LabTest labTest) {
        return Optional.ofNullable(labTest)
                .map(l -> LabTestResponseDTO.builder()
                        .id(l.getId())
                        .testName(l.getTestName())
                        .patient(PatientMapper.toDto(l.getPatient()))
                        .result(l.getResult())
                        .testDate(l.getTestDate())
                        .nurse(NurseMapper.toDto(l.getNurse()))
                        .build())
                .orElse(null);
    }

    public static LabTest toEntity(LabTestDTO labTestDTO, Patients patient) {
        return Optional.ofNullable(labTestDTO)
                .map(dto -> {
                    LabTest labTest = new LabTest();
                    labTest.setPatient(patient);
                    labTest.setTestName(dto.getTestName());
                    labTest.setResult(dto.getResult());
                    labTest.setTestDate(dto.getTestDate());
                    return labTest;
                })
                .orElse(null);
    }
    public static List<LabTestResponseDTO> toDtoList(List<LabTest> list) {
        return list == null ? null : list.stream()
                .map(LabTestMapper::toResponse)
                .collect(Collectors.toList());
    }
}
