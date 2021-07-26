package com.company.controller.database;

public class MongoCredentials {

    private final String user;
    private final String password;
    private final String cluster;
    public final String dbName;

    public MongoCredentials(String user, String password, String cluster, String dbName) {
        this.user = user;
        this.password = password;
        this.cluster = cluster;
        this.dbName = dbName;
    }

    public String getConnectionURI() {
        String uri = "mongodb+srv://";
        uri = uri.concat(this.user);
        uri = uri.concat(":");
        uri = uri.concat(this.password);
        uri = uri.concat("@");
        uri = uri.concat(this.cluster);
        uri = uri.concat("/");
        uri = uri.concat(this.dbName);
        uri = uri.concat("?retryWrites=true&w=majority");

        return uri;
    }

    public String getDbName() {
        return dbName;
    }
}

