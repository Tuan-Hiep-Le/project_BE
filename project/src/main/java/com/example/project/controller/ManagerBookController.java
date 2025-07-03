package com.example.project.controller;

import com.example.project.entity.Book;
import com.example.project.entity.BookDocument;
import com.example.project.service.ManagerBookServiceImpl;
import com.example.project.service.SearchBookService;
import com.example.project.service.SearchBookServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping("/manager_book")
public class ManagerBookController {
    @Autowired
    private SearchBookServiceImpl service;
    @Autowired
    private ManagerBookServiceImpl managerBookService;
    @GetMapping()
    public Page<Book> getAllBookInStore(Pageable pageable){
        return managerBookService.getAllBook(pageable);
    }

    @GetMapping("/search")
    public Page<BookDocument> searchBookByKeyword(@RequestParam String key,Pageable pageable){
        return service.searchBookByNameBookAndAuthorAndCategoryAndTopic(key,pageable);
    }
}
