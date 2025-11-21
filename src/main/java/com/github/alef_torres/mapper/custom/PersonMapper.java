package com.github.alef_torres.mapper.custom;

import com.github.alef_torres.data.dto.v2.PersonDTOV2;
import com.github.alef_torres.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setBirthDate(new Date());
        return dto;
    }

    public Person convertDTOToEntity(PersonDTOV2 personDTOV2) {
        Person entity = new Person();
        entity.setFirstName(personDTOV2.getFirstName());
        entity.setLastName(personDTOV2.getLastName());
        entity.setAddress(personDTOV2.getAddress());
        return entity;
    }
}
