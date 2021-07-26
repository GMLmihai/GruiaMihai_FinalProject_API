package com.company.model.deleteChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteChatroomRequest {
    private final String chatroomName;
    private final String password;

    public DeleteChatroomRequest(@JsonProperty("chatroomName") String chatroomName,
                                 @JsonProperty("password") String password) {
        this.chatroomName = chatroomName;
        this.password = password;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getPassword() {

        return password;
    }
}
