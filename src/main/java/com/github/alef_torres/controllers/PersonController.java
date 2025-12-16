package com.github.alef_torres.controllers;

import com.github.alef_torres.controllers.docs.PersonControllerDocs;
import com.github.alef_torres.data.dto.v1.PersonDTOV1;
import com.github.alef_torres.data.dto.v2.PersonDTOV2;
import com.github.alef_torres.file.exporter.factory.MediaTypes;
import com.github.alef_torres.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/person")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices personServices;

    @GetMapping(value = "/export/{id}",
            produces = {
                    MediaType.APPLICATION_PDF_VALUE}
    )
    @Override
    public ResponseEntity<Resource> export(@PathVariable("id") Long id, HttpServletRequest request) throws Exception {

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        Resource file = personServices.exportPerson(id, acceptHeader);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(acceptHeader))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=person.pdf")
                .body(file);
    }

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
                    MediaType.APPLICATION_JSON_VALUE
            })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTOV1>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        return ResponseEntity.ok(personServices.findAll(Pageable));
    }

    @RequestMapping(
            value = "/exportPage",
            method = RequestMethod.GET,
            produces = {
                    MediaTypes.APPLICATION_CSV_VALUE,
                    MediaTypes.APPLICATION_XLSX_VALUE,
                    MediaTypes.APPLICATION_PDF_VALUE
            })
    @Override
    public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = personServices.exportPage(pageable, acceptHeader);
        Map<String, String> extensionMap = Map.of(
                MediaTypes.APPLICATION_PDF_VALUE, ".pfd",
                MediaTypes.APPLICATION_CSV_VALUE, ".csv",
                MediaTypes.APPLICATION_XLSX_VALUE, ".xls"
        );

        var fileExtension = extensionMap.getOrDefault(acceptHeader, "");
        var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";
        var fileName = "people_exported" + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(file);

    }

    @GetMapping(value = "/findPeopleByName/{firstName}", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTOV1>>> findByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(personServices.findByName(firstName, pageable));
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<PersonDTOV1> create(@RequestBody PersonDTOV1 personDTOV1) {
        return ResponseEntity.ok(personServices.create(personDTOV1));
    }

    @RequestMapping(value = "/massCreation",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<List<PersonDTOV1>> massCreation(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(personServices.massCreation(file));
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
