package com.example.project.controller;

import com.example.project.entity.Book;
import com.example.project.entity.Order;
import com.example.project.entity.Review;
import com.example.project.entity.User;
import com.example.project.service.impl.ManagerBookServiceImpl;
import com.example.project.service.impl.ManagerOrderServiceImpl;
import com.example.project.service.impl.ManagerReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ManagerReviewController {
    @Autowired
    private ManagerReviewServiceImpl managerReviewService;
    @Autowired
    private ManagerBookServiceImpl managerBookService;
    @Autowired
    private ManagerOrderServiceImpl managerOrderService;
    @PostMapping("/information_book/write_review")
    public String userWriteReview(@RequestParam("bookId") Integer bookId,@RequestParam("contentReview") String content, @RequestParam("reviewStar") int reviewStar, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Book book = managerBookService.getBookById(bookId);
        model.addAttribute("contentReview",content);
        model.addAttribute("reviewStar",reviewStar);
        model.addAttribute("bookId",bookId);
        Order  order = managerOrderService.getOrderByCondition(user.getUserId(), bookId);
        Review review = Review.builder().user(user).book(book).starRate(reviewStar).comment(content).reviewAt(LocalDateTime.now()).order(order).build();
        managerReviewService.addReview(review);
        return "redirect:/homepage/information_book?bookid=" + bookId;
    }
}
