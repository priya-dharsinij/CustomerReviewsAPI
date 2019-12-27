package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.model.Review;

import java.util.List;

public interface ReviewService {
    Review save(Long productId,Review review);
    Iterable<com.udacity.course3.reviews.document.Review> findByProductIdAndRating(Long productId, int filterByRating);
    List<com.udacity.course3.reviews.document.Review> findByProductId(Long productId, String sortBy);
}
