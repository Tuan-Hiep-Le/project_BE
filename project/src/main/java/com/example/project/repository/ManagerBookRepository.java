package com.example.project.repository;

import com.example.project.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerBookRepository extends JpaRepository<Book,Integer> {

    Page<Book> findByAuthor(String nameAuthor, Pageable pageable);

    Page<Book> findByCategory(String nameCategory, Pageable pageable);

    Page<Book> findByTopic(String nameTopic, Pageable pageable);
}
