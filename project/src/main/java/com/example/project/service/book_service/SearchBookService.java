package com.example.project.service.book_service;

import com.example.project.entity.BookDocument;
import com.example.project.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchBookService {
    //Tìm kiếm sách theo tên sách
    public List<BookDocument> searchBookByName (String nameBook);
    //Tìm kiếm sách theo tên tác giả
    public Page<BookDocument> searchBookByNameAuthor(String nameBook, Pageable pageable);
    //Tìm kiếm sách theo tên loại
    public Page<BookDocument> searchBookByNameCategory(String nameCategory, Pageable pageable);
    //Tìm kiếm sách theo tên chủ đề
    public Page<BookDocument> searchBookByNameTopic(String nameTopic, Pageable pageable);
    //Tìm kiếm theo cả tên sách, tác giả, loại, chủ đề
    public Page<BookDocument> searchBookByNameBookAndAuthorAndCategoryAndTopic(String keyword, Pageable pageable);

}
