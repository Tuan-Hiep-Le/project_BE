package com.example.project.service.impl;

import com.example.project.entity.Review;
import com.example.project.repository.ReviewerRepository;
import com.example.project.service.ManagerReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ManagerReviewServiceImpl implements ManagerReviewService {
    @Autowired
    private ReviewerRepository reviewerRepository;
    @Override
    public List<Review> getUserAndCommentBook(Integer id) {
        return reviewerRepository.getAllUserAndComment(id);
    }
}
