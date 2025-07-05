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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@Transactional
public class ManagerBookController {
    @Autowired
    private SearchBookServiceImpl searchBookService;
    @Autowired
    private ManagerBookServiceImpl managerBookService;

    //Đồng bộ data của Book cho BookDocument
    @GetMapping("/admin/sync-book")
    public String syncDataToElasticsearch(Model model,Pageable pageable) {
        searchBookService.syncAllBooksToES(pageable);
        model.addAttribute("message", "Đã đồng bộ dữ liệu lên Elasticsearch thành công!");
        return "redirect:/home";
    }
    //Trang Chủ
    @GetMapping("/homepage")
    public String getAllBookInStore(Pageable pageable, Model model){
        Page<Book> allProduct = managerBookService.getAllBook(pageable);
        List<String> listAuthor = managerBookService.getAllAuthor();
        List<String> listCategory = managerBookService.getAllCategory();
        List<String> listTopic = managerBookService.getAllTopic();
        model.addAttribute("books",allProduct);
        model.addAttribute("authors",listAuthor);
        model.addAttribute("categories",listCategory);
        model.addAttribute("topics",listTopic);

        return "home";
    }

    //Tìm kiếm sách
    @GetMapping("homepage/search")
    public String searchBookByKeyword(@RequestParam(value = "keyword",required = false) String key,Pageable pageable,Model model){
        Page<BookDocument> bookDocuments = searchBookService.searchBookByNameBookAndAuthorAndCategoryAndTopic(key,pageable);
        model.addAttribute("books",bookDocuments);
        model.addAttribute("keyword",key);
        return "home";
    }

    //Phân loại sách theo thể loại
    @GetMapping("homepage/category")
    public String classifyBookByCategory(@RequestParam("category") String nameCategory, Pageable pageable,Model model){
        Page<Book> listBook = managerBookService.findByCategory(nameCategory,pageable);
        List<String> listCategory = managerBookService.getAllCategory();
        model.addAttribute("books",listBook);
        model.addAttribute("category",nameCategory);
        model.addAttribute("categories", listCategory);
        return "home";
    }

    //Phân loại Sách theo tác giả
    @GetMapping("homepage/author")
    public String classifyBookByAuthor(@RequestParam("name_author") String nameAuthor, Pageable pageable, Model model){
        Page<Book> listBook = managerBookService.findByAuthor(nameAuthor,pageable);
        List<String> listAuthor = managerBookService.getAllAuthor();
        model.addAttribute("books",listBook);
        model.addAttribute("name_author",nameAuthor);
        model.addAttribute("authors",listAuthor);
        return "home";
    }

    //Phân loại sách theo chủ đề
    @GetMapping("homepage/topic")
    public String classifyBookByTopic(@RequestParam("name_topic") String nameTopic, Pageable pageable, Model model){
        Page<Book> listBook = managerBookService.findByTopic(nameTopic,pageable);
        List<String> listTopic = managerBookService.getAllTopic();
        model.addAttribute("books",listBook);
        model.addAttribute("name_topic",nameTopic);
        model.addAttribute("topics",listTopic);
        return "home";
    }






}
