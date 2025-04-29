package org.health.se7a.security;

import org.health.se7a.security.util.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final List<UserService> userRepositories;

    public void create(UserRegisterRequest request) {
        userRepositories.stream()
                .filter(userService -> userService.getType().equals(request.getType()))
                .findFirst()
                .orElseThrow(NotImplementedException::new)
                .create(request);
    }
}
