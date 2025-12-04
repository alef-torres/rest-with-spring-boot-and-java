package com.github.alef_torres.unitetests.services;

import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import com.github.alef_torres.data.dto.v2.PersonDTOV2;
import com.github.alef_torres.exception.RequiredObjectIsNullException;
import com.github.alef_torres.exception.ResourceNotFoundException;
import com.github.alef_torres.mapper.custom.PersonMapper;
import com.github.alef_torres.model.Person;
import com.github.alef_torres.repository.PersonRepository;
import com.github.alef_torres.services.PersonServices;
import com.github.alef_torres.unitetests.mapper.mocks.MockPerson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock
    PersonRepository repository;

    @Mock
    PersonMapper personMapper;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {

        Person person = input.mockEntity(1);
        person.setId(1L);
        PersonDTOV1 dto = input.mockDTO(1);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        var result = service.findByID(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("GET")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("collection") && link.getHref().endsWith("/person/v1") && link.getType().equals("GET")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("DELETE")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/person/v1") && link.getType().equals("POST")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/person/v1") && link.getType().equals("PUT")));


        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testFindByIdWithNonExistentId() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findByID(1L);
        });

        String expectedMessage = "Person not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void create() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTOV1 dto = input.mockDTO(1);

        when(repository.save(person)).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("GET")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("collection") && link.getHref().endsWith("/person/v1") && link.getType().equals("GET")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("DELETE")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/person/v1") && link.getType().equals("POST")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/person/v1") && link.getType().equals("PUT")));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void createv2() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        PersonDTOV2 dto = input.mockDTOV2(1);

        when(personMapper.convertDTOToEntity(dto)).thenReturn(person);
        when(repository.save(person)).thenReturn(person);
        when(personMapper.convertEntityToDTO(person)).thenReturn(dto);

        var result = service.createv2(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getBirthDate());

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreatev2WithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.createv2(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        Person persisted = person;
        persisted.setId(1L);

        PersonDTOV1 dto = input.mockDTO(1);
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("GET")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("collection") && link.getHref().endsWith("/person/v1") && link.getType().equals("GET")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("DELETE")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/person/v1") && link.getType().equals("POST")));
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/person/v1") && link.getType().equals("PUT")));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateWithNonExistentId() {
        PersonDTOV1 dto = input.mockDTO(1);
        when(repository.findById(dto.getId())).thenReturn(Optional.empty());


        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto);
        });

        String expectedMessage = "Person not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testDeleteWithNonExistentId() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1L);
        });

        String expectedMessage = "Person not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findAll() {
        List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PersonDTOV1> people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        var personOne = people.get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getId());
        assertNotNull(personOne.getLinks());

        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("GET")));
        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("collection") && link.getHref().endsWith("/person/v1") && link.getType().equals("GET")));
        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/person/v1/1") && link.getType().equals("DELETE")));
        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/person/v1") && link.getType().equals("POST")));
        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/person/v1") && link.getType().equals("PUT")));

        assertEquals("Address Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());

        var personFour = people.get(4);

        assertNotNull(personFour);
        assertNotNull(personFour.getId());
        assertNotNull(personFour.getLinks());

        assertTrue(personFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/4") && link.getType().equals("GET")));
        assertTrue(personFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("collection") && link.getHref().endsWith("/person/v1") && link.getType().equals("GET")));
        assertTrue(personFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/person/v1/4") && link.getType().equals("DELETE")));
        assertTrue(personFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/person/v1") && link.getType().equals("POST")));
        assertTrue(personFour.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/person/v1") && link.getType().equals("PUT")));

        assertEquals("Address Test4", personFour.getAddress());
        assertEquals("First Name Test4", personFour.getFirstName());
        assertEquals("Last Name Test4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());

        var personSeven = people.get(7);

        assertNotNull(personSeven);
        assertNotNull(personSeven.getId());
        assertNotNull(personSeven.getLinks());

        assertTrue(personSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self") && link.getHref().endsWith("/person/v1/7") && link.getType().equals("GET")));
        assertTrue(personSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("collection") && link.getHref().endsWith("/person/v1") && link.getType().equals("GET")));
        assertTrue(personSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete") && link.getHref().endsWith("/person/v1/7") && link.getType().equals("DELETE")));
        assertTrue(personSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create") && link.getHref().endsWith("/person/v1") && link.getType().equals("POST")));
        assertTrue(personSeven.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update") && link.getHref().endsWith("/person/v1") && link.getType().equals("PUT")));

        assertEquals("Address Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Last Name Test7", personSeven.getLastName());
        assertEquals("Female", personSeven.getGender());
    }
}