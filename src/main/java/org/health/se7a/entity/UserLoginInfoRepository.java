package org.health.se7a.entity;

import org.health.se7a.security.model.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginInfoRepository extends JpaRepository<UserLoginInfo, Long> {
    boolean existsByTelNumber(String telNumber);

    Optional<UserLoginInfo> findByTelNumber(String telNumber);

    @Query("SELECT u.loginType FROM UserLoginInfo u WHERE u.telNumber = :telNumber")
    LoginType getLoginTypeByTelNumber(@Param("telNumber") String telNumber);
}
