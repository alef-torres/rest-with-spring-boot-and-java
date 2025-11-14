package com.github.alef_torres;

import com.github.alef_torres.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findByID(String id) {
        logger.info("Finding person with id: " + id);
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Alef");
        person.setLastName("Strogulski");
        person.setAddress("Rua torta");
        person.setGender("M");
        return person;
    }

    public List<Person> findAll() {
        logger.info("Finding all people");
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }

    public Person create(Person person) {
        logger.info("Creating person with id: " + person.getId());
        return person;
    }

    public Person update(Person person) {
        logger.info("Updating person with id: " + person.getId());
        return person;
    }

    public void delete(String id) {
        logger.info("Deleting one person.");
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("First Name " + i);
        person.setLastName("Last Name " + i);
        person.setAddress("Rua torta");
        person.setGender("M");
        return person;
    }
}
