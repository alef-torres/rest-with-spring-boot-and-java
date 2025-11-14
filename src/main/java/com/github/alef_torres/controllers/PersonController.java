package com.github.alef_torres.controllers;

import com.github.alef_torres.model.Person;
import com.github.alef_torres.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController {

    @Autowired
    private PersonServices personServices;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds a Person", description = "Finds a Person", tags = {"People"})
    public Person findById(@PathVariable("id") Long id) {
        return personServices.findByID(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds all People", description = "Finds all People", tags = {"People"})
    public List<Person> findAll() {
        return personServices.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a new Person", description = "Adds a new Person by passing in a JSON representation of the person!", tags = {"People"})
    public Person create(@RequestBody Person person) {
        return personServices.create(person);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a Person", description = "Updates a Person by passing in a JSON representation of the person!", tags = {"People"})
    public Person update(@RequestBody Person person) {
        return personServices.update(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Deletes a Person", description = "Deletes a Person by their ID", tags = {"People"})
    public void delete(@PathVariable("id") Long id) {
        personServices.delete(id);
    }


}
