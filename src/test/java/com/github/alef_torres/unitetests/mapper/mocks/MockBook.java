package com.github.alef_torres.unitetests.mapper.mocks;

import com.github.alef_torres.data.dto.v1.BookDTOV1;
import com.github.alef_torres.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookDTOV1 mockDTO() {
        return mockDTO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTOV1> mockDTOList() {
        List<BookDTOV1> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Some Author" + number);
        book.setLaunchDate(new Date());
        book.setPrice(25D);
        book.setTitle("Some Title" + number);
        return book;
    }

    public BookDTOV1 mockDTO(Integer number) {
        BookDTOV1 book = new BookDTOV1();
        book.setId(number.longValue());
        book.setAuthor("Some Author" + number);
        book.setLaunch_date(new Date());
        book.setPrice(25D);
        book.setTitle("Some Title" + number);
        return book;
    }

}
