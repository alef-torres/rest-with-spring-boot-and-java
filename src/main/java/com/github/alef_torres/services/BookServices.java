package com.github.alef_torres.services;

import com.github.alef_torres.controllers.BookController;
import com.github.alef_torres.data.dto.v1.BookDTOV1;
import com.github.alef_torres.mapper.ObjectMapper;
import com.github.alef_torres.model.Book;
import com.github.alef_torres.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private final Logger logger = LoggerFactory.getLogger(BookServices.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PagedResourcesAssembler<BookDTOV1> pagedResourcesAssembler;

    @Transactional
    public BookDTOV1 findById(long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new ResourceAccessException("Book not found"));
        logger.info("Finding a book");
        var dto = ObjectMapper.parseObject(book, BookDTOV1.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PagedModel<EntityModel<BookDTOV1>> findAll(Pageable pageable) {
        var books = bookRepository.findAll(pageable);
        Page<BookDTOV1> booksWithLinks = books.map(book -> {
            var dto = ObjectMapper.parseObject(book, BookDTOV1.class);
            addHateoasLinks(dto);
            return dto;
        });
        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())
        )).withSelfRel();
        logger.info("Finding all books with params");
        return pagedResourcesAssembler.toModel(booksWithLinks, findAllLink);
    }

    @Transactional
    public BookDTOV1 save(BookDTOV1 book) {
        var bookNew = ObjectMapper.parseObject(book, Book.class);
        bookRepository.save(bookNew);
        logger.info("Creating a book");
        return ObjectMapper.parseObject(bookNew, BookDTOV1.class);
    }

    @Transactional
    public BookDTOV1 updateBook(BookDTOV1 book) {
        var bookUpdate = bookRepository.findById(book.getId()).orElseThrow(() -> new ResourceAccessException("Book not found"));
        logger.info("Update a book data");
        bookUpdater(book, bookUpdate);
        return ObjectMapper.parseObject(bookUpdate, BookDTOV1.class);
    }

    @Transactional
    public void deleteBook(long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
            logger.info("Deleting a book");
        }
    }

    private void bookUpdater(BookDTOV1 book, Book bookUpdate) {
        bookUpdate.setAuthor(book.getAuthor());
        bookUpdate.setTitle(book.getTitle());
        bookUpdate.setPrice(book.getPrice());
        bookUpdate.setLaunch_date(book.getLaunch_date());
        bookRepository.save(bookUpdate);
    }

    private static void addHateoasLinks(BookDTOV1 dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll(1, 12, "asc")).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withSelfRel().withType("DELETE"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withSelfRel().withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withSelfRel().withType("PUT"));
    }

}
