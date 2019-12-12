package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review , Long> {

    List<Review> findByProductId(Long productID);
}
