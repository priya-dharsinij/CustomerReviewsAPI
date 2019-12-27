package com.udacity.course3.reviews.service;

import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.model.Product;
import com.udacity.course3.reviews.model.Review;
import com.udacity.course3.reviews.mongoRepository.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewMongoRepository reviewMongoRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates a review for a product in both MySQL and MongoDB.
     * <p>
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @Override
    public Review save(Long productId, Review review) {
        Review newReview = productRepository.findById(productId).map(product -> {
            review.setProduct(product);
            //save review to MySQL repository
            Review savedReview = reviewRepository.save(review);

            //save review to MongoRepository
            com.udacity.course3.reviews.document.Review mongoReview = new com.udacity.course3.reviews.document.Review();
            mongoReview.setId(savedReview.getId());
            mongoReview.setProductId(productId);
            mongoReview.setUserName(savedReview.getUserName());
            mongoReview.setUserEmail(savedReview.getUserEmail());
            mongoReview.setHeadLine(savedReview.getHeadLine());
            mongoReview.setRating(savedReview.getRating());
            mongoReview.setText(savedReview.getText());
            mongoReview.setCreated(savedReview.getCreated());
            reviewMongoRepository.save(mongoReview);

            //update product ratings as new reviews ar added
            updateProductRatings(product);

            return savedReview;
        }).orElseThrow(() -> new ResourceNotFoundException("ProductID " + productId + " not found"));

        return newReview;
    }

    @Override
    public Iterable<com.udacity.course3.reviews.document.Review> findByProductIdAndRating(Long productId, int filterByRating) {
        return reviewMongoRepository.findByProductIdAndRating(productId,filterByRating);
    }

    @Override
    public List<com.udacity.course3.reviews.document.Review> findByProductId(Long productId, String sortBy) {
        Sort sortOrder = Sort.by(sortBy);
        return reviewMongoRepository.findByProductId(productId,sortOrder.descending());
    }

    /**
     * Update product ratings when new reviews are created.
     *
     * @param product The product.
     * @return void.
     */
    private void updateProductRatings(Product product){
        List<Review> reviewList = reviewRepository.findByProductId(product.getId());

        OptionalDouble average = reviewList.stream().mapToDouble(rev -> rev.getRating()).average();
        BigDecimal bigDecimal = new BigDecimal(average.getAsDouble()).setScale(1, RoundingMode.HALF_UP);

        product.setRatings(bigDecimal.floatValue());
        productRepository.save(product);
    }
}
