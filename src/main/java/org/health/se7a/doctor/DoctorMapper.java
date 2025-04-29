package org.health.se7a.doctor;

import java.util.Optional;

public class DoctorMapper {

    public static DoctorDTO toDto(Doctor doctor) {
        return Optional.ofNullable(doctor)
                .map(d -> DoctorDTO.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .telNumber(d.getTelNumber())
                        .specialty(d.getSpecialty())
                        .licenceNumber(d.getLicenceNumber())
                        .build())
                .orElse(null);
    }

    public static Doctor toEntity(DoctorDTO dto) {
        return Optional.ofNullable(dto)
                .map(d -> Doctor.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .telNumber(d.getTelNumber())
                        .specialty(d.getSpecialty())
                        .licenceNumber(d.getLicenceNumber())
                        .build())
                .orElse(null);
    }
}
