package org.health.se7a.security.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByUserSecurityIdAndUuid(String userId, String uuid);

    Optional<OTP> findByUuid(String uuid);

    @Query(value = "SELECT COUNT(*) FROM otps WHERE initialized_at > CURRENT_TIMESTAMP - INTERVAL '24 HOUR' AND user_security_id=:userId", nativeQuery = true)
    Integer countByUserSecurityIdAndInitializedAtBetween(String userId);
}
