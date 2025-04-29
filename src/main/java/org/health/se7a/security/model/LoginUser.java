package org.health.se7a.security.model;


public interface LoginUser {

    Long getId();

    String getTelNumber();

    String getName();

    LoginType getType();

    AccountStatus getAccountStatus();

    void setAccountStatus(AccountStatus accountStatus);


}
