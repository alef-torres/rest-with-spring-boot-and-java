package com.github.alef_torres.file.exporter.contract;

import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTOV1> people) throws Exception;

    Resource exportPerson(PersonDTOV1 person) throws Exception;
}
