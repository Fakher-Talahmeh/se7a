package org.health.se7a.security.otp;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "otps")
public class OTP {

    @Id
    @GeneratedValue(generator = "otps_id_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "otps_id_sequence", sequenceName = "otps_id_sequence", allocationSize = 1)
    private Long id;

    private String code;

    private String uuid;
    private String userSecurityId;
    private Integer numberOfVerifications;

    @Enumerated(EnumType.STRING)
    private OTPStatus otpStatus;

    private LocalDateTime initializedAt;
}
