package com.github.alef_torres.services;

import com.github.alef_torres.model.Person;
import com.github.alef_torres.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PersonRepository personRepository;

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Transactional
    public Person findByID(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Person not found"));
    }

    @Transactional
    public List<Person> findAll() {
        logger.info("Finding all people");
        return personRepository.findAll();
    }

    @Transactional
    public Person create(Person person) {
        logger.info("Creating a new Person");
        return personRepository.save(person);
    }

    @Transactional
    public Person update(Person person) {
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceAccessException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return personRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Person not found"));
        personRepository.delete(entity);
        logger.info("Deleting one person.");
    }
}
