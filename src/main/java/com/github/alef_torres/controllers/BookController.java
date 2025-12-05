package com.github.alef_torres.controllers;

import com.github.alef_torres.controllers.docs.BookControllerDocs;
import com.github.alef_torres.data.dto.v1.BookDTOV1;
import com.github.alef_torres.services.BookServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Tag(name = "Books", description = "Endpoints for Managing Books")
public class BookController implements BookControllerDocs {

    @Autowired
    private BookServices bookServices;

    @GetMapping(value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<BookDTOV1> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(bookServices.findById(id));
    }

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<PagedModel<EntityModel<BookDTOV1>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        return ResponseEntity.ok().body(bookServices.findAll(Pageable));
    }

    @PostMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<BookDTOV1> create(@RequestBody BookDTOV1 bookDTO) {
        return ResponseEntity.ok().body(bookServices.save(bookDTO));
    }

    @PutMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<BookDTOV1> update(@RequestBody BookDTOV1 bookDTO) {
        return ResponseEntity.ok().body(bookServices.updateBook(bookDTO));
    }

    @DeleteMapping(value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    @Override
    public ResponseEntity<BookDTOV1> delete(@PathVariable long id) {
        bookServices.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
