package com.usuario.controller;

import com.usuario.model.Person;
import com.usuario.repository.IPersonRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
public class AuthenticatorController{

    @Autowired
    IPersonRepository iPersonRepository;

    private final SecretKey CHAVE = Keys.hmacShaKeyFor(
            "7f-j&CKk=coNzZc0y7_4obMP?#TfcYq%fcD0mDpenW2nc!lfGoZ|d?f&RNbDHUX6"
                    .getBytes(StandardCharsets.UTF_8));

    Logger logger = LoggerFactory.getLogger(AuthenticatorController.class);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Person person){
        try {
            List<Person> listPerson = iPersonRepository.findByEmail(person.getEmail());


            if(listPerson.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            if(!listPerson.get(0).getPassword().equals(person.getPassword())){
                return ResponseEntity.badRequest().build();
            }

            String jwtToken = Jwts.builder()
                    .setSubject(listPerson.get(0).getRole())
                    .setIssuedAt(new Date())
                    .setExpiration(
                            Date.from(LocalDateTime.now().plusMinutes(2L)
                                    .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(CHAVE).compact();

            return new ResponseEntity<>("{\"token\": \"" + jwtToken + "\"}", HttpStatus.OK);

        }catch (Exception e){
            logger.error("Erro ao encontar usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }

}
