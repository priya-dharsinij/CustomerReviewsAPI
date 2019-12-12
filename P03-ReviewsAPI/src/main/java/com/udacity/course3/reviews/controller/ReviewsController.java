package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.model.Product;
import com.udacity.course3.reviews.model.Review;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    // TODO: Wire JPA repositories here

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates a review for a product.
     * <p>
     * 1. Add argument for review entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of product.
     * 3. If product not found, return NOT_FOUND.
     * 4. If found, save review.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Review> createReviewForProduct(@PathVariable("productId") Long productId, @Valid @RequestBody Review review) {

        Review newReview = productRepository.findById(productId).map(product -> {
            review.setProduct(product);
            Review savedReview = reviewRepository.save(review);
            //update product ratings as new reviews ar added
            updateProductRatings(product);

            return savedReview;
        }).orElseThrow(() -> new ResourceNotFoundException("ProductID " + productId + " not found"));

        return ResponseEntity.ok(newReview);

    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<Review>> listReviewsForProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewRepository.findByProductId(productId));
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