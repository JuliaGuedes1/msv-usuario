package com.usuario.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class Interceptor {

    Logger logger = LoggerFactory.getLogger(Interceptor.class);

    private final SecretKey CHAVE =
            Keys.hmacShaKeyFor("7f-j&CKk=coNzZc0y7_4obMP?#TfcYq%fcD0mDpenW2nc!lfGoZ|d?f&RNbDHUX6"
                    .getBytes(StandardCharsets.UTF_8));


    public boolean validate(String token){

        if(token == null){
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public boolean validateRole(String token, String requirement) {

        if (token == null) {
            return false;
        }

        try {
            Claims tokenDecoded = Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (tokenDecoded.get("role", String.class).equals(requirement)) {
                return true;
            }
            return false;
        }catch (Exception e){
            logger.error("Token invalido", e);
            return false;
        }

    }

    public boolean validateOwnId(String token, Long id){
        if (token == null) {
            return false;
        }

        try {
            Claims tokenDecoded = Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (tokenDecoded.get("id", Long.class).equals(id)) {
                return true;
            }
            return false;
        }catch (Exception e){
            logger.error("Token invalido", e);
            return false;
        }
    }

}
