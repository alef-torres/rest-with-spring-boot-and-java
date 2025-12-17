package com.github.alef_torres.services;

import com.github.alef_torres.controllers.PersonController;
import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import com.github.alef_torres.data.dto.v2.PersonDTOV2;
import com.github.alef_torres.exception.BadRequestException;
import com.github.alef_torres.exception.FileStorageException;
import com.github.alef_torres.exception.RequiredObjectIsNullException;
import com.github.alef_torres.exception.ResourceNotFoundException;
import com.github.alef_torres.file.exporter.contract.PersonExporter;
import com.github.alef_torres.file.exporter.factory.FileExporterFactory;
import com.github.alef_torres.file.importer.contract.FileImporter;
import com.github.alef_torres.file.importer.factory.FileImporterFactory;
import com.github.alef_torres.mapper.custom.PersonMapper;
import com.github.alef_torres.model.Person;
import com.github.alef_torres.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.alef_torres.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServices {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private FileImporterFactory fileImporterFactory;

    @Autowired
    private FileExporterFactory  fileExporterFactory;

    @Autowired
    private PagedResourcesAssembler<PersonDTOV1> pagedResourcesAssembler;

    private final Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Transactional
    public PersonDTOV1 findByID(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        logger.info("Finding a people");
        var dto = parseObject(person, PersonDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public Resource exportPerson(Long id, String acceptHeader) throws Exception {
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        logger.info("Exporting a data of one person!");
        var dto = parseObject(person, PersonDTOV1.class);
        PersonExporter exporter = this.fileExporterFactory.getExporter(acceptHeader);
        return exporter.exportPerson(dto);
    }

    @Transactional
    public Resource exportPage(Pageable pageable, String acceptHeader) {
        var people = personRepository.findAll(pageable).map(p -> parseObject(p, PersonDTOV1.class)).getContent();
        logger.info("Exporting a people page");
        try {
            PersonExporter exporter = this.fileExporterFactory.getExporter(acceptHeader);
            return exporter.exportPeople(people);
        } catch (Exception e) {
            throw new RuntimeException("Error exporting people page", e);
        }
    }

    @Transactional
    public PagedModel<EntityModel<PersonDTOV1>> findAll(Pageable pageable) {
        var people = personRepository.findAll(pageable);
        return buildPagedModel(people, pageable, "Finding all people with params");
    }

    @Transactional
    public PagedModel<EntityModel<PersonDTOV1>> findByName(String firstName, Pageable pageable) {
        var people = personRepository.findPeopleByName(firstName, pageable);
        return buildPagedModel(people, pageable, "Finding People by name.");
    }

    @Transactional
    public PersonDTOV1 create(PersonDTOV1 personDTOV1) {
        if (personDTOV1 == null) throw new RequiredObjectIsNullException();
        logger.info("Creating a new Person");
        Person person = parseObject(personDTOV1, Person.class);
        personRepository.save(person);
        var dto = parseObject(person, PersonDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public List<PersonDTOV1> massCreation(MultipartFile file) throws Exception {
        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid File!");

        try (InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter importer = this.fileImporterFactory.getImporter(filename);

            List<Person> entities = importer.importFile(inputStream).stream()
                    .map(dto -> personRepository.save(parseObject(dto, Person.class)))
                    .toList();

            return entities.stream()
                    .map(entity -> {
                        var dto = parseObject(entity, PersonDTOV1.class);
                        addHateoasLinks(dto);
                        return dto;
                    })
                    .toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
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
        Person person = parseObject(personDTOV1, Person.class);
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        personRepository.save(entity);
        logger.info("Updating a Person {}", person.getId());
        var dto = parseObject(entity, PersonDTOV1.class);
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
        var dto = parseObject(entity, PersonDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    private static void addHateoasLinks(PersonDTOV1 dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("collection").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("", 1, 12, "asc")).withRel("collection").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(1, 12, "asc", null)).withRel("exportPage").withType("GET").withTitle("Export").withSelfRel());
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("massCreation").withRel("massCreation").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
    }

    private PagedModel<EntityModel<PersonDTOV1>> buildPagedModel(Page<Person> people, Pageable pageable, String Finding_all_people_with_params) {
        Page<PersonDTOV1> peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTOV1.class);
            addHateoasLinks(dto);
            return dto;
        });
        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())
        )).withSelfRel();
        logger.info(Finding_all_people_with_params);
        return pagedResourcesAssembler.toModel(peopleWithLinks, findAllLink);
    }
}
