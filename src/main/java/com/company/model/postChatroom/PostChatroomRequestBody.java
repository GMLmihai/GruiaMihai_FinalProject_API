package com.company.model.postChatroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostChatroomRequestBody {

    private final String name;
    private final String password;

    public PostChatroomRequestBody(@JsonProperty("name") String name, @JsonProperty("password") String password) {
        this.name = name;
        this.password = password;

    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return password;
    }
}
