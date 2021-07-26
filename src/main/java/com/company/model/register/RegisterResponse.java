package com.company.model.register;

public class RegisterResponse {

    private final String registeredID;

    public RegisterResponse(String registeredID) {
        this.registeredID = registeredID;
    }

    public String getRegisteredID() {
        return this.registeredID;
    }
}
