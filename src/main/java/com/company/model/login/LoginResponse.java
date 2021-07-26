package com.company.model.login;

public class LoginResponse {

    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
