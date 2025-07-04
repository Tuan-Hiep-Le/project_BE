package com.example.project.repository;

import com.example.project.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagerBookRepository extends JpaRepository<Book,Integer> {

    Page<Book> findByAuthor(String nameAuthor, Pageable pageable);

    Page<Book> findByCategory(String nameCategory, Pageable pageable);

    Page<Book> findByTopic(String nameTopic, Pageable pageable);

    //Lấy ra danh sách tác giả
    @Query("SELECT DISTINCT b.author FROM Book b ")
    public List<String> listAuthor();

    //Lấy ra danh sách thể loại
    @Query("SELECT DISTINCT b.category FROM Book b")
    public List<String> listCategory();

    @Query("SELECT DISTINCT b.topic FROM Book b")
    public List<String> listTopic();
}
