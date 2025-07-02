package com.example.project.repository.jpa;

import com.example.project.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerBookRepository extends JpaRepository<Book,Integer> {
    //Tìm kiếm sách theo tên tác giả
    @Query("SELECT b FROM Book b WHERE b.author = :name_author")
    public Page<Book> findByNameAuthor(@Param("name_author") String nameAuthor, Pageable pageable);

    //Tìm kiếm sách theo category
    @Query("SELECT b FROM Book b WHERE b.category = :name_category")
    public Page<Book> findByNameCategory(@Param("name_category") String nameCategory, Pageable pageable);

    //Tìm kiếm sách theo topic
    @Query("SELECT b FROM Book b WHERE b.topic = :name_topic")
    public Page<Book> findByNameTopic(@Param("name_topic") String nameTopic, Pageable pageable);
}
