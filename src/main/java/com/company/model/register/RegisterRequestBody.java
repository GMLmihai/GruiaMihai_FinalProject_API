package com.company.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestBody {

    private final String username;
    private final String password;

    public RegisterRequestBody(@JsonProperty("username") String username,
                               @JsonProperty("password") String password){
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
