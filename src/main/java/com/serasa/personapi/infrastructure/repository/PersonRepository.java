package com.serasa.personapi.infrastructure.repository;

import com.serasa.personapi.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByIdAndActiveTrue(Long id);
}
