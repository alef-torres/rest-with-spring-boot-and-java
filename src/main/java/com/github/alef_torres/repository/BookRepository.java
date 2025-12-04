package com.github.alef_torres.repository;

import com.github.alef_torres.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
