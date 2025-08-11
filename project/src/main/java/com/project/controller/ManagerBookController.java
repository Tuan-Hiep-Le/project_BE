package com.project.controller;

import com.project.entity.*;
import com.project.entity.*;
import com.project.entity.elastic.BookDocument;
import com.project.service.impl.*;
import com.project.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class ManagerBookController {
    @Autowired
    private SearchBookServiceImpl searchBookService;
    @Autowired
    private ManagerBookServiceImpl managerBookService;

    @Autowired
    private ManagerReviewServiceImpl managerReviewService;
    @Autowired
    private ManagerCartItemServiceImpl managerCartItemService;
    @Autowired
    private ManagerOrderServiceImpl managerOrderService;



    //Đồng bộ data của Book cho BookDocument
    @GetMapping("/admin/sync-book")
    public String syncDataToElasticsearch(Model model) {
        searchBookService.syncAllBooksToES();
        model.addAttribute("message", "Đã đồng bộ dữ liệu lên Elasticsearch thành công!");
        return "redirect:/homepage";
    }
    //Trang Chủ Trước Khi Đăng Nhập
//    @GetMapping("/homepage")
//    public String getAllBookInStore(Pageable pageable, Model model,@RequestParam(value = "nameCategory",required = false) String category, @RequestParam(value = "nameAuthor", required = false) String nameAuthor,@RequestParam(value = "nameTopic",required = false) String nameTopic){
//        boolean isFiltering = (category != null && !category.isEmpty()) || (nameAuthor != null && !nameAuthor.isEmpty()) || (nameTopic != null && !nameTopic.isEmpty());
//        Page<Book> pageResult = isFiltering ? managerBookService.filterBooks(category, nameAuthor, nameTopic, pageable) : managerBookService.getAllBook(pageable);
//        List<String> listAuthor = managerBookService.getAllAuthor();
//        List<String> listCategory = managerBookService.getAllCategory();
//        List<String> listTopic = managerBookService.getAllTopic();
//        model.addAttribute("nameCategory",category);
//        model.addAttribute("nameAuthor",nameAuthor);
//        model.addAttribute("nameTopic",nameTopic);
//        model.addAttribute("books",pageResult);
//        model.addAttribute("authors",listAuthor);
//        model.addAttribute("categories",listCategory);
//        model.addAttribute("topics",listTopic);
//
//        return "home";
//    }

//    //Tìm kiếm sách
//    @GetMapping("/homepage/search")
//    public String searchBookByKeyword(@RequestParam(value = "keyword",required = false) String key,@RequestParam(value = "valuePage", defaultValue = "0") int valuePage,@RequestParam(value = "valueSize",defaultValue = "10") int valueSize,Model model){
//        Pageable pageable = PageRequest.of(valuePage,valueSize);
//        Page<BookDocument> bookDocuments = searchBookService.searchBookByNameBookAndAuthorAndCategoryAndTopic(key,pageable);
//        if(bookDocuments.isEmpty()){
//            model.addAttribute("notFound",true);
//        }else {
//            model.addAttribute("notFound",false);
//        }
//        model.addAttribute("books",bookDocuments);
//        model.addAttribute("keyword",key);
//        model.addAttribute("valuePage",valuePage);
//        model.addAttribute("valueSize",valuePage);
//        return "home";
//    }

//    //Phân loại sách theo thể loại
//    @GetMapping("/homepage/category")
//    public String classifyBookByCategory(@RequestParam("category") String nameCategory, Pageable pageable,Model model){
//        Page<Book> listBook = managerBookService.findByCategory(nameCategory,pageable);
//        List<String> listCategory = managerBookService.getAllCategory();
//        model.addAttribute("books",listBook);
//        model.addAttribute("category",nameCategory);
//        model.addAttribute("categories", listCategory);
//        return "home";
//    }
    //Phân loại sách theo tác giả
//    @GetMapping("/homepage/author")
//    public String classifyBookByAuthor(@RequestParam("name_author") String nameAuthor, Pageable pageable, Model model){
//        Page<Book> listBook = managerBookService.findByAuthor(nameAuthor,pageable);
//        List<String> listAuthor = managerBookService.getAllAuthor();
//        model.addAttribute("books",listBook);
//        model.addAttribute("name_author",nameAuthor);
//        model.addAttribute("authors",listAuthor);
//        return "home";
//    }
    //Phân loại sách theo chủ đề
//    @GetMapping("/homepage/topic")
//    public String classifyBookByTopic(@RequestParam("name_topic") String nameTopic, Pageable pageable, Model model){
//        Page<Book> listBook = managerBookService.findByTopic(nameTopic,pageable);
//        List<String> listTopic = managerBookService.getAllTopic();
//        model.addAttribute("books",listBook);
//        model.addAttribute("name_topic",nameTopic);
//        model.addAttribute("topics",listTopic);
//        return "home";
//    }

    //Trang Chủ Sau Khi Đăng Nhập
    @GetMapping("/homepage")
    public String homeAfterLogin(@RequestParam(value = "nameCategory",required = false) String category, @RequestParam(value = "nameAuthor", required = false) String nameAuthor, @RequestParam(value = "nameTopic",required = false) String nameTopic,@RequestParam(value = "valuePage",defaultValue = "0") int valuePage, @RequestParam(value = "valueSize",defaultValue = "10") int valueSize, Model model){
        Pageable pageable = PageRequest.of(valuePage,valueSize);
        boolean isFiltering = (category != null && !category.isEmpty()) || (nameAuthor != null && !nameAuthor.isEmpty()) || (nameTopic != null && !nameTopic.isEmpty());
        Page<Book> pageResult = isFiltering ? managerBookService.filterBooks(category, nameAuthor, nameTopic, pageable) : managerBookService.getAllBook(pageable);
        List<String> listAuthor = managerBookService.getAllAuthor();
        List<String> listCategory = managerBookService.getAllCategory();
        List<String> listTopic = managerBookService.getAllTopic();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin",true);
            User user = (User) authentication.getPrincipal();
            List<CartItem> list = managerCartItemService.getAllCTByUserId(user.getUserId());
            int countCartItem = list.size();
            if (user.getAvatar() != null) {
                model.addAttribute("hasAvatar",true);
                model.addAttribute("avatar",user.getAvatar());
            }else {
                model.addAttribute("hasAvatar",false);
            }
            model.addAttribute("countCartItem",countCartItem);
        }

        model.addAttribute("books",pageResult);
        model.addAttribute("authors",listAuthor);
        model.addAttribute("categories",listCategory);
        model.addAttribute("topics",listTopic);
        model.addAttribute("valuePage",valuePage);
        model.addAttribute("valueSize",valueSize);
        model.addAttribute("totalPage",(pageResult.getTotalPages()));

        model.addAttribute("nameCategory",category);
        model.addAttribute("nameAuthor",nameAuthor);
        model.addAttribute("nameTopic",nameTopic);

        return"home_after_login";
    }
    //Tìm kiếm sách sau login
    @GetMapping("/homepage/search")
    public String searchBookByKeywordInHomeUser(@RequestParam(value = "keyword",required = false) String key,@RequestParam(value = "valuePage", defaultValue = "0") int valuePage,@RequestParam(value = "valueSize",defaultValue = "10") int valueSize,Model model){
        Pageable pageable = PageRequest.of(valuePage,valueSize);
        Page<BookDocument> bookDocuments = searchBookService.searchBookByNameBookAndAuthorAndCategoryAndTopic(key,pageable);
        if(bookDocuments.isEmpty()){
            model.addAttribute("notFound",true);
        }else {
            model.addAttribute("notFound",false);
        }
        model.addAttribute("books",bookDocuments);
        model.addAttribute("keyword",key);
        model.addAttribute("valuePage",valuePage);
        model.addAttribute("valueSize",valueSize);
        model.addAttribute("totalPage",bookDocuments.getTotalPages());
        return "home_after_login";
    }
    //Phân loại sách theo thể loại sau login
    @GetMapping("/homepage/category")
    public String classifyBookByCategoryInHomeUser(@RequestParam("category") String nameCategory, @RequestParam(value = "valuePage", defaultValue = "0") int valuePage,@RequestParam(value = "valueSize",defaultValue = "10") int valueSize,Model model){
        Pageable pageable = PageRequest.of(valuePage,valueSize);
        Page<Book> listBook = managerBookService.findByCategory(nameCategory,pageable);
        List<String> listCategory = managerBookService.getAllCategory();
        model.addAttribute("books",listBook);
        model.addAttribute("category",nameCategory);
        model.addAttribute("categories", listCategory);
        model.addAttribute("valuePage", valuePage);
        model.addAttribute("valueSize", valueSize);
        model.addAttribute("totalPage", listBook.getTotalPages());


        return "home_after_login";
    }

    //Phân loại Sách theo tác giả
    @GetMapping("/homepage/author")
    public String classifyBookByAuthorInHomeUser(@RequestParam("name_author") String nameAuthor, @RequestParam(value = "valuePage", defaultValue = "0") int valuePage,@RequestParam(value = "valueSize",defaultValue = "10") int valueSize, Model model){
        Pageable pageable = PageRequest.of(valuePage,valueSize);
        Page<Book> listBook = managerBookService.findByAuthor(nameAuthor,pageable);
        List<String> listAuthor = managerBookService.getAllAuthor();
        model.addAttribute("books",listBook);
        model.addAttribute("name_author",nameAuthor);
        model.addAttribute("authors",listAuthor);
        model.addAttribute("valuePage",valuePage);
        model.addAttribute("valueSize",valueSize);
        model.addAttribute("totalPage",listBook.getTotalPages());

        return "home_after_login";
    }
    //Phân loại sách theo chủ đề sau login
    @GetMapping("/homepage/topic")
    public String classifyBookByTopicInHomeUser(@RequestParam("name_topic") String nameTopic, @RequestParam(value = "valuePage", defaultValue = "0") int valuePage,@RequestParam(value = "valueSize",defaultValue = "10") int valueSize, Model model){
        Pageable pageable = PageRequest.of(valuePage,valueSize);
        Page<Book> listBook = managerBookService.findByTopic(nameTopic,pageable);
        List<String> listTopic = managerBookService.getAllTopic();
        model.addAttribute("books",listBook);
        model.addAttribute("name_topic",nameTopic);
        model.addAttribute("topics",listTopic);
        model.addAttribute("valuePage",valuePage);
        model.addAttribute("valueSize",valueSize);
        model.addAttribute("totalPage",listBook.getTotalPages());
        return "home_after_login";
    }

    @GetMapping("/homepage/information_book")
    public String informationBook(@RequestParam("bookid") Integer idBook, Model model){
        Book book = managerBookService.getBookById(idBook);
        model.addAttribute("product",book);
        List<Review> list = managerReviewService.getUserAndCommentBook(idBook);
        if(list.isEmpty()){
            model.addAttribute("noReview",true);
        }else {
            model.addAttribute("noReview",false);
        }
        model.addAttribute("reviews",list);
        model.addAttribute("id_book",idBook);
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            model.addAttribute("is_review_book", true);
            return "product_detail";
        }
        User user = (User) authentication.getPrincipal();
        Order order = managerOrderService.getOrderByCondition(user.getUserId(), idBook);
        boolean isReviewBook = true;

        if (order != null && order.getReview() == null) {

            isReviewBook = false;
        }
        model.addAttribute("is_review_book", isReviewBook);
        return "product_detail";
    }








}
