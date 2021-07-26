package com.company.controller.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;

import java.util.Date;
import java.util.Map;

public class TokenManager {
    private final String privateKey;
    private final String publicKey;
    private final String signature;
    private final long expireInSeconds = 172800;


    public TokenManager(String privateKey, String publicKey, String signature) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    public TokenManager() {
        Map<String, String> env = System.getenv();
        this.privateKey = env.get("AUTH_PRIVATE_KEY");
        this.publicKey = env.get("AUTH_PUBLIC_KEY");
        this.signature = env.get("AUTH_SIGNATURE");
    }


    public String generateToken(String username) {

        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        long now = new Date().getTime();
        long expireTime = now + (expireInSeconds);
        Date expireDate = new Date(expireTime);

        String jwtToken = JWT.create()
                .withIssuer("JWT")
                .withIssuer("Mihai")
                .withClaim("username", username)
                .withExpiresAt(expireDate)
                .sign(algorithm);
        return jwtToken;
    }


    public Map<String, Claim> verifyToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("JWT")
                    .withIssuer("Mihai")
                    .acceptExpiresAt(expireInSeconds)
                    .build();
            return verifier.verify(token).getClaims();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getSignature() {
        return signature;
    }


}
