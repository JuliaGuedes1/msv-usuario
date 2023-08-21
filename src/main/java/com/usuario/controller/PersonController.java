package com.usuario.controller;

import com.usuario.model.Person;
import com.usuario.model.PersonDTO;
import com.usuario.repository.IPersonRepository;
import com.usuario.util.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "person/")
public class PersonController {

    private final IPersonRepository iPersonRepository;

    private Interceptor interceptor;

    Logger logger = LoggerFactory.getLogger(PersonController.class);


    public PersonController(IPersonRepository iPersonRepository) {
        this.iPersonRepository = iPersonRepository;
        this.interceptor = new Interceptor();
    }

    @GetMapping("find-all-person")
    public ResponseEntity<List<PersonDTO>> getAllPerson(@RequestHeader("Authorization") String token){

        /*if(!interceptor.validate(token)){
            return ResponseEntity.badRequest().build();
        }*/

        if(interceptor.validateRole(token, "admin")){
            logger.info("O usuario eh administrador");
        }
        if(interceptor.validateRole(token, "seller")){
            logger.info("Voce eh seller");
        }
        if(interceptor.validateRole(token, "buyer")){
            logger.info("Voce eh buyer");
        }

        try {
            List<Person> personList = iPersonRepository.findAll();
            List<PersonDTO> personWithoutString = new ArrayList<>();

            PersonDTO personDTO = new PersonDTO();
            for(Person person: personList){
                personDTO.setId(person.getId());
                personDTO.setAge(person.getAge());
                personDTO.setEmail(person.getEmail());
                personDTO.setFirstName(person.getFirstName());
                personDTO.setLastName(person.getLastName());
                personDTO.setRole(person.getRole());
                personWithoutString.add(personDTO);
            }

            return new ResponseEntity<>(personWithoutString, HttpStatus.OK);
        }catch (Exception e){
            logger.error("Erro ao encontrar os usuarios", e);

            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("find/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id, @RequestHeader("Authorization") String token){

        if(!interceptor.validate(token)){
            return ResponseEntity.badRequest().build();
        }

        try {
            Person person = iPersonRepository.findById(id).get();

            PersonDTO personWithoutString = new PersonDTO();
            personWithoutString.setId(person.getId());
            personWithoutString.setFirstName(person.getFirstName());
            personWithoutString.setLastName(person.getLastName());
            personWithoutString.setAge(person.getAge());
            personWithoutString.setEmail(person.getEmail());
            personWithoutString.setRole(person.getRole());


            return new ResponseEntity<>(personWithoutString, HttpStatus.OK);
        }catch (Exception e){
            logger.error("Erro ao encontrar o usuario", e);

            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save-person")
    public ResponseEntity savePerson(@RequestBody Person person, @RequestHeader("Authorization") String token){

        if(!interceptor.validate(token)){
            return ResponseEntity.badRequest().build();
        }

        try {
            iPersonRepository.save(person);
            logger.info("Usuario cadastrado com sucesso!");
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            logger.error("Erro ao salvar usuario", e);

            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


    }

    @PutMapping("update-person/{id}")
    public ResponseEntity updatePerson(@RequestBody Person person, @PathVariable Long id, @RequestHeader("Authorization") String token){

        if(!interceptor.validate(token)){
            return ResponseEntity.badRequest().build();
        }

        try {
            Person person1 = iPersonRepository.findById(id).get();
            person1.setFirstName(person.getFirstName());
            person1.setLastName(person.getLastName());
            person1.setAge(person.getAge());
            person1.setEmail(person.getEmail());
            person1.setPassword(person.getPassword());
            person1.setRole(person.getRole());

            logger.info("Usuario atualizado");

            return new ResponseEntity(iPersonRepository.save(person1), HttpStatus.OK);

        }catch (Exception e){
            logger.error("Erro ao atualizar usuario", e);

            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deletePerson(@PathVariable Long id, @RequestHeader("Authorization") String token){

        if(!interceptor.validate(token)){
            return ResponseEntity.badRequest().build();
        }

        try {
            iPersonRepository.deleteById(id);

            logger.info("Usuario deletado com sucesso");

            return new ResponseEntity(HttpStatus.OK);

        }catch (Exception e){
            logger.error("Erro ao deletar usuario", e);
            return ResponseEntity.badRequest().build();

        }

    }

}
