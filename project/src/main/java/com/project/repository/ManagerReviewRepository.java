package com.project.repository;

import com.project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerReviewRepository extends JpaRepository<Review,Integer> {
    //Lấy ra tất cả các người dùng đã bình luận của họ trong 1 sản phẩm
    @Query("SELECT r FROM Review r WHERE r.book.bookId = :id_book Order By r.reviewAt DESC")
    public List<Review> getAllUserAndComment(@Param("id_book") Integer id);


}
