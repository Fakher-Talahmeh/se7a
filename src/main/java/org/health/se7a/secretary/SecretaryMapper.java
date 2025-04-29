package org.health.se7a.secretary;

import java.util.Optional;

public class SecretaryMapper {
    public static SecretaryDTO toDto(Secretary secretary) {
        return Optional.ofNullable(secretary)
                .map(s -> SecretaryDTO.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .telNumber(s.getTelNumber())
                        .build())
                .orElse(null);
    }

    public static Secretary toEntity(SecretaryDTO dto) {
        return Optional.ofNullable(dto)
                .map(d -> Secretary.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .telNumber(d.getTelNumber())
                        .build())
                .orElse(null);
    }
}
