package com.example.project.repository;

import com.example.project.entity.Book;
import com.example.project.entity.Review;
import com.example.project.entity.User;
import com.example.project.entity.enum_entity.Language;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

public interface ManagerBookRepository extends JpaRepository<Book,Integer> {
    Book findByBookId(Integer id);

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
