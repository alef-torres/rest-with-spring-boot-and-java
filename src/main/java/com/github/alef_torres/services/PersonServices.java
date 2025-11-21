package com.github.alef_torres.services;

import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import com.github.alef_torres.data.dto.v2.PersonDTOV2;
import com.github.alef_torres.mapper.ObjectMapper;
import com.github.alef_torres.mapper.custom.PersonMapper;
import com.github.alef_torres.model.Person;
import com.github.alef_torres.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    private final Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Transactional
    public PersonDTOV1 findByID(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Person not found"));
        logger.info("Finding a people");
        return ObjectMapper.parseObject(person, PersonDTOV1.class);
    }

    @Transactional
    public List<PersonDTOV1> findAll() {
        logger.info("Finding all people");
        return ObjectMapper.parseListObjects(personRepository.findAll(), PersonDTOV1.class);
    }

    @Transactional
    public PersonDTOV1 create(PersonDTOV1 personDTOV1) {
        logger.info("Creating a new Person");
        Person person = ObjectMapper.parseObject(personDTOV1, Person.class);
        personRepository.save(person);
        return ObjectMapper.parseObject(person, PersonDTOV1.class);
    }

    @Transactional
    public PersonDTOV2 createv2(PersonDTOV2 personDTOV2) {
        logger.info("Creating a new Person V2");
        Person person = personMapper.convertDTOToEntity(personDTOV2);
        personRepository.save(person);
        return personMapper.convertEntityToDTO(person);
    }

    @Transactional
    public PersonDTOV1 update(PersonDTOV1 personDTOV1) {
        Person person = ObjectMapper.parseObject(personDTOV1, Person.class);
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceAccessException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        personRepository.save(entity);
        logger.info("Updating a Person {}", person.getId());
        return ObjectMapper.parseObject(person, PersonDTOV1.class);
    }

    @Transactional
    public void delete(Long id) {
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Person not found"));
        personRepository.delete(entity);
        logger.info("Deleting one person.");
    }
}
