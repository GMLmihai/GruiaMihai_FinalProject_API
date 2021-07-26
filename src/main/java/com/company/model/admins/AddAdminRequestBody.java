package com.company.model.admins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddAdminRequestBody {
    private final String chatroomName;
    private final String adminName;

    public AddAdminRequestBody(@JsonProperty("chatroomName") String chatroomName,
                               @JsonProperty("adminName") String adminName) {
        this.chatroomName = chatroomName;
        this.adminName = adminName;


    }

    public String getChatroomName() {
        return this.chatroomName;
    }

    public String getAdminName() {
        return this.adminName;
    }

}