package com.company.model.getChatroomMessages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatroomMessageRequestBody {

    private final String chatroomName;
    private final String message;
    private final String password;

    public ChatroomMessageRequestBody(@JsonProperty("chatroomName") String chatroomName,
                                      @JsonProperty("message") String message,
                                      @JsonProperty("password") String password) {
        this.chatroomName = chatroomName;
        this.message = message;
        this.password = password;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getMessage() {
        return message;
    }

    public String getPassword() {
        return password;
    }
}
