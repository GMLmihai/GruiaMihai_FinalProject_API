package com.company.model.postChatroom;

public class PostChatroomResponse {

    private final String insertedId;

    public PostChatroomResponse(String insertedId) {
        this.insertedId = insertedId;
    }

    public String getInsertedId() {
        return insertedId;
    }

}
