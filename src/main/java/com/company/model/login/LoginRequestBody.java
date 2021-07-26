package com.company.model.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestBody {

    private final String username;
    private final String password;

    public LoginRequestBody(@JsonProperty("username") String username,
                            @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
