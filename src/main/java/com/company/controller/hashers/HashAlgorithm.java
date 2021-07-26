package com.company.controller.hashers;

public interface HashAlgorithm {

    String hash(String passwordToHash);

    String genSalt();

    String saltAndHash(String str);

    boolean checkPassword(String hashPass, String candidate);

}


