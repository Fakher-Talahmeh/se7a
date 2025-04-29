package org.health.se7a.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoggedUserDto {
    private Long id;
    private LoginType type;
    private String username;

}
