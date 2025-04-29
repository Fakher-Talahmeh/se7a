package org.health.se7a.security.util;

import org.health.se7a.security.UserRegisterRequest;
import org.health.se7a.security.model.LoginType;

public interface UserService {

    void create(UserRegisterRequest request);

    LoginType getType();
}
