package com.github.alef_torres.services;

import com.github.alef_torres.controllers.PersonController;
import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import com.github.alef_torres.data.dto.v2.PersonDTOV2;
import com.github.alef_torres.exception.RequiredObjectIsNullException;
import com.github.alef_torres.exception.ResourceNotFoundException;
import com.github.alef_torres.mapper.ObjectMapper;
import com.github.alef_torres.mapper.custom.PersonMapper;
import com.github.alef_torres.model.Person;
import com.github.alef_torres.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PagedResourcesAssembler<PersonDTOV1>  pagedResourcesAssembler;

    private final Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Transactional
    public PersonDTOV1 findByID(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        logger.info("Finding a people");
        var dto = ObjectMapper.parseObject(person, PersonDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PagedModel<EntityModel<PersonDTOV1>> findAll(Pageable pageable) {
        var people = personRepository.findAll(pageable);
        Page<PersonDTOV1> peopleWithLinks = people.map(person -> {
            var dto = ObjectMapper.parseObject(person, PersonDTOV1.class);
            addHateoasLinks(dto);
            return dto;
        });
        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())
                )).withSelfRel();
        logger.info("Finding all people with params");
        return pagedResourcesAssembler.toModel(peopleWithLinks, findAllLink);
    }

    @Transactional
    public PersonDTOV1 create(PersonDTOV1 personDTOV1) {
        if (personDTOV1 == null) throw new RequiredObjectIsNullException();
        logger.info("Creating a new Person");
        Person person = ObjectMapper.parseObject(personDTOV1, Person.class);
        personRepository.save(person);
        var dto = ObjectMapper.parseObject(person, PersonDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTOV2 createv2(PersonDTOV2 personDTOV2) {
        if (personDTOV2 == null) throw new RequiredObjectIsNullException();
        logger.info("Creating a new Person V2");
        Person person = personMapper.convertDTOToEntity(personDTOV2);
        personRepository.save(person);
        return personMapper.convertEntityToDTO(person);
    }

    @Transactional
    public PersonDTOV1 update(PersonDTOV1 personDTOV1) {
        if (personDTOV1 == null) throw new RequiredObjectIsNullException();
        Person person = ObjectMapper.parseObject(personDTOV1, Person.class);
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        personRepository.save(entity);
        logger.info("Updating a Person {}", person.getId());
        var dto = ObjectMapper.parseObject(entity, PersonDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        personRepository.delete(entity);
        logger.info("Deleting one person.");
    }

    @Transactional
    public PersonDTOV1 disablePerson(Long id) {
        personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        logger.info("Disabling Person id {}.", id);
        personRepository.disablePerson(id);
        var entity = personRepository.findById(id).get();
        var dto = ObjectMapper.parseObject(entity, PersonDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    private static void addHateoasLinks(PersonDTOV1 dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1,12, "asc")).withRel("collection").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
    }
}
