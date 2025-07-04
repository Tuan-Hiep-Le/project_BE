package com.example.project.controller;

import com.example.project.entity.Book;
import com.example.project.entity.BookDocument;
import com.example.project.service.ManagerBookServiceImpl;
import com.example.project.service.SearchBookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@Transactional
@RequestMapping("/manager_book")
public class ManagerBookController {
    @Autowired
    private SearchBookServiceImpl searchBookService;
    @Autowired
    private ManagerBookServiceImpl managerBookService;
    @GetMapping("/home")
    public String getAllBookInStore(Pageable pageable, Model model){
        Page<Book> allProduct = managerBookService.getAllBook(pageable);
        List<String> listAuthor = managerBookService.getAllAuthor();
        List<String> listCategory = managerBookService.getAllCategory();
        List<String> listTopic = managerBookService.getAllTopic();
        model.addAttribute("books",allProduct);
        model.addAttribute("authors",listAuthor);
        model.addAttribute("categories",listCategory);
        model.addAttribute("topics",listTopic);


        return "product";
    }

    @GetMapping("/search")
    public Page<BookDocument> searchBookByKeyword(@RequestParam String key,Pageable pageable){
        return searchBookService.searchBookByNameBookAndAuthorAndCategoryAndTopic(key,pageable);
    }
}
