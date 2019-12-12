package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.model.Comment;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    // TODO: Wire needed JPA repositories here

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Creates a comment for a review.
     *
     * 1. Add argument for comment entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, save comment.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<Comment> createCommentForReview(@PathVariable("reviewId") Long reviewId, @Valid @RequestBody Comment comment) {
        Comment savedComment = reviewRepository.findById(reviewId).map(review -> {
            comment.setReview(review);
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("ReviewID " + reviewId + " not found"));

        return ResponseEntity.ok(savedComment);
    }

    /**
     * List comments for a review.
     *
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, return list of comments.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public List<Comment> listCommentsForReview(@PathVariable("reviewId") Long reviewId) {
        return commentRepository.findByReviewId(reviewId);
    }
}