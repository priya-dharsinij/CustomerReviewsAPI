package com.udacity.course3.reviews.mongoRepository;

import com.udacity.course3.reviews.document.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewMongoRepository extends MongoRepository<Review, Long> {
    List<Review> findByProductId(Long productID);

    List<Review> findByProductId(Long productID, Sort sortOrder);

    List<Review> findByProductIdAndRating(Long productID, int rating);
}
