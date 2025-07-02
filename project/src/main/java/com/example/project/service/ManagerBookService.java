package com.example.project.service;

import com.example.project.entity.Book;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagerBookService {
    //Thêm sách
    public Book addBook(Book book);

    // Xóa sách
    public void removeBook(Integer id);

    //Xóa số lượng sách
    public void removeQuantityBook(Integer id, int quantityRemove);

    //Chỉnh sửa sách
    public Book updateBook(Book book);

    //Tìm kiếm sách theo tên sách
    public Page<Book> findByNameBook(String nameBook, Pageable pageable);

    //Tìm kiếm sách theo tên tác giả
    public Page<Book> findByAuthor(String nameAuthor, Pageable  pageable);

    //Tìm kiếm sách theo category
    public Page<Book> findByCategory(String nameCategory, Pageable pageable);

    //Tìm kiếm sách theo topic
    public Page<Book> findByTopic(String nameTopic, Pageable pageable);
}
