package com.example.project.service;

import com.example.project.entity.Review;

import java.util.List;

public interface ManagerReviewService {
    //Lấy ra tất cả người dùng và bình luận trong 1 cuốn sách
    public List<Review> getUserAndCommentBook(Integer id);
}
