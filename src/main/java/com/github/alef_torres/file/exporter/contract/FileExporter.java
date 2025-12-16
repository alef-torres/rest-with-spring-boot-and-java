package com.github.alef_torres.file.exporter.contract;

import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTOV1> people) throws Exception;

    Resource exportPerson(PersonDTOV1 person) throws Exception;
}
