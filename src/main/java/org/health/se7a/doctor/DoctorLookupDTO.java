package org.health.se7a.doctor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorLookupDTO {
    private Long id;
    private String fullName;
}