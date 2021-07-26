package com.company.model.admins;

import java.util.ArrayList;

public class GetAdminsResponse {
    private final ArrayList<String> admins;

    public GetAdminsResponse(ArrayList<String> admins) {
        this.admins = admins;
    }

    public ArrayList<String> getAdmins() {
        return admins;
    }
}
