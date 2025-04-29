package org.health.se7a.nurse;

import java.util.Optional;

public class NurseMapper {

    public static NurseDTO toDto(Nurse nurse) {
        return Optional.ofNullable(nurse)
                .map(n -> NurseDTO.builder()
                        .id(n.getId())
                        .name(n.getName())
                        .telNumber(n.getTelNumber())
                        .build())
                .orElse(null);
    }

    public static Nurse toEntity(NurseDTO dto) {
        return Optional.ofNullable(dto)
                .map(d -> Nurse.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .telNumber(d.getTelNumber())
                        .build())
                .orElse(null);
    }
}
