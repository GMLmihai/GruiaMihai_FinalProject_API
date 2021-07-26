package com.company.controller.hashers;

import com.company.utils.Constants;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


public class Sha512 implements HashAlgorithm {

    @Override
    public String hash(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public String genSalt() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = Constants.SALT_LENGTH;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public String saltAndHash(String str) {
        String salt = genSalt();
        return salt + hash(salt + str);
    }

    @Override
    public boolean checkPassword(String hashPass, String candidate) {
        String str = hashPass.substring(Constants.SALT_LENGTH);
        String hash = hash(hashPass.substring(0, Constants.SALT_LENGTH) + candidate);
        return hash.equals(str);
    }
}
