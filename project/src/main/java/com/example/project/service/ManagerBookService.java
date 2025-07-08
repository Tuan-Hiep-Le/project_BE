package com.example.project.service;

import com.example.project.entity.Book;
import com.example.project.entity.BookDocument;
import com.example.project.entity.Review;
import com.example.project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ManagerBookService {
    //Lấy toàn bộ sách
    public Page<Book> getAllBook(Pageable pageable);
    //Thêm sách
    public Book addBook(Book book);

    // Xóa sách
    public void removeBook(Integer id);

    //Xóa số lượng sách
    public void removeQuantityBook(Integer id, int quantityRemove);

    //Chỉnh sửa sách
    public Book updateBook(Book book);

    //Phân loại sách theo tên tác giả
    public Page<Book> findByAuthor(String nameAuthor, Pageable  pageable);

    //Phân loại sách theo category
    public Page<Book> findByCategory(String nameCategory, Pageable pageable);

    //Phân loại sách theo topic
    public Page<Book> findByTopic(String nameTopic, Pageable pageable);
    //Lấy tất cả tác giả
   public List<String> getAllAuthor();
   //Lấy tất cả thể loại
   public List<String> getAllCategory();
   //Lấy tất cả chủ đề
   public List<String> getAllTopic();

    //Lấy sách bằng Id
    public Book getBookById(Integer id);

    //Lấy ra tất cả sachs
    public List<Book> getAllBookList();




}
