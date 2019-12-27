package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.model.Review;
import com.udacity.course3.reviews.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    // TODO: Wire JPA repositories here

    @Autowired
    private ReviewService reviewService;

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
        return ResponseEntity.ok(reviewService.save(productId,review));
    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product and Sort by column.
     * @return The list of reviews sorted by the requested column.
     *         By default, the reviews will be sorted by 'created' with most recent reviews on top.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<com.udacity.course3.reviews.document.Review>> listReviewsForProduct(@PathVariable("productId") Long productId, @RequestParam(defaultValue = "created") String sortBy, @RequestParam(required = false) Integer filterByRating) {

        if(filterByRating!= null && filterByRating > 0){
            return ResponseEntity.ok(reviewService.findByProductIdAndRating(productId,filterByRating));
        }
        return ResponseEntity.ok(reviewService.findByProductId(productId,sortBy));
    }

}