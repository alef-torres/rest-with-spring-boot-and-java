package com.github.alef_torres.file.importer.contract;

import com.github.alef_torres.data.dto.v1.PersonDTOV1;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTOV1> importFile(InputStream inputStream) throws Exception;
}
