package com.pomegranate.pomemusic.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pomegranate.pomemusic.model.User;


@Service
public class TokenService {
    
    @Value("${jwt.secret}")
    private String secretkey;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretkey);

            String token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getEmail())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);

            return token;

        }catch (JWTCreationException exception){
            throw new RuntimeException("Error creating token");
        }
    }

    public String validateToken (String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretkey);
            return JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}
