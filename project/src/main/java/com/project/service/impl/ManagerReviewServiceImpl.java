package com.project.service.impl;

import com.project.entity.Review;
import com.project.repository.ManagerReviewRepository;
import com.project.service.ManagerReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ManagerReviewServiceImpl implements ManagerReviewService {
    @Autowired
    private ManagerReviewRepository reviewerRepository;
    @Override
    public List<Review> getUserAndCommentBook(Integer id) {
        return reviewerRepository.getAllUserAndComment(id);
    }

    @Override
    public Review addReview(Review review) {
        return reviewerRepository.saveAndFlush(review);
    }
}
