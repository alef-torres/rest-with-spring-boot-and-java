package com.github.alef_torres.controllers;

import com.github.alef_torres.controllers.docs.PersonControllerDocs;
import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import com.github.alef_torres.data.dto.v2.PersonDTOV2;
import com.github.alef_torres.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/person")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices personServices;

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
            })
    @Override
    public ResponseEntity<PersonDTOV1> findById(@PathVariable("id") Long id) {
        var person = personServices.findByID(id);
        person.setBirthDate(new Date());
        person.setPhone("99-999999999");
        person.setSensitiveData("sensitive");
        return ResponseEntity.ok(person);
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
            })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTOV1>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(personServices.findAll(Pageable));
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<PersonDTOV1> create(@RequestBody PersonDTOV1 personDTOV1) {
        return ResponseEntity.ok(personServices.create(personDTOV1));
    }

    @RequestMapping(value = "/v2", method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<PersonDTOV2> create(@RequestBody PersonDTOV2 personDTOV2) {
        return ResponseEntity.ok(personServices.createv2(personDTOV2));
    }

    @RequestMapping(method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<PersonDTOV1> update(@RequestBody PersonDTOV1 personDTOV1) {
        return ResponseEntity.ok(personServices.update(personDTOV1));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<PersonDTOV1> disablePerson(@PathVariable("id") Long id) {
        var personUpdate = personServices.disablePerson(id);
        return ResponseEntity.ok(personUpdate);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE
    )
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        personServices.delete(id);
        return ResponseEntity.ok().build();
    }
}
