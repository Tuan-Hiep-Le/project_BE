package com.project.controller;

import com.project.entity.Book;
import com.project.entity.Order;
import com.project.entity.Review;
import com.project.entity.User;
import com.project.service.impl.ManagerBookServiceImpl;
import com.project.service.impl.ManagerOrderServiceImpl;
import com.project.service.impl.ManagerReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String userWriteReview(@RequestParam("bookId") Integer bookId,@RequestParam("contentReview") String content, @RequestParam("reviewStar") int reviewStar, Model model, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Book book = managerBookService.getBookById(bookId);
        model.addAttribute("contentReview",content);
        model.addAttribute("reviewStar",reviewStar);
        model.addAttribute("bookId",bookId);
        Order order = managerOrderService.getOrderByCondition(user.getUserId(), bookId);
        if (order != null && order.getReview() == null) {
            Review review = Review.builder().user(user).book(book).starRate(reviewStar).comment(content).reviewAt(LocalDateTime.now()).order(order).build();
            managerReviewService.addReview(review);
        }else {
            redirectAttributes.addFlashAttribute("is_review_book",true);
        }
        return "redirect:/homepage/information_book?bookid=" + bookId;
    }
}
