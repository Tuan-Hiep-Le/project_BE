package com.example.project.repository;

import com.example.project.entity.Book;
import com.example.project.entity.enum_entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerBookRepository extends JpaRepository<Book,Integer> {

    Page<Book> findByNameAuthor(String nameAuthor, Pageable pageable);

    Page<Book> findByNameCategory(String nameCategory, Pageable pageable);

    Page<Book> findByNameTopic(String nameTopic, Pageable pageable);

    Book findByNameBookAndNameAuthorAndLanguage(String nameBook, String nameAuthor, Language language);

    //Lấy ra danh sách tác giả
    @Query("SELECT DISTINCT b.nameAuthor FROM Book b ")
    public List<String> listAuthor();

    //Lấy ra danh sách thể loại
    @Query("SELECT DISTINCT b.nameCategory FROM Book b")
    public List<String> listCategory();

    @Query("SELECT DISTINCT b.nameTopic FROM Book b")
    public List<String> listTopic();


}
