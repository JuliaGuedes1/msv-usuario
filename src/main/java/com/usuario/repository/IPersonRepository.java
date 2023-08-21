package com.usuario.repository;


import com.usuario.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByEmail(String email);

}
