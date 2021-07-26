package com.company.model.updateChatroomName;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateChatroomNameRequestBody {

    private final String chatroomName;
    private final String newChatroomName;
    private final String password;


    public UpdateChatroomNameRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                         @JsonProperty("newChatroomName") String newChatroomName,
                                         @JsonProperty("password") String password) {
        this.chatroomName = chatroomName;
        this.newChatroomName = newChatroomName;
        this.password = password;

    }

    public String getChatroomName() {
        return this.chatroomName;
    }

    public String getNewChatroomName() {
        return this.newChatroomName;
    }

    public String getPassword() {
        return password;
    }
}