package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.document.Review;
import com.udacity.course3.reviews.exception.ResourceNotFoundException;
import com.udacity.course3.reviews.model.Comment;
import com.udacity.course3.reviews.mongoRepository.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    private ReviewMongoRepository reviewMongoRepository;

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
            Comment newComment = commentRepository.save(comment);
            //update Review document to add comment in MongoDB
            updateCommentForReview(newComment,reviewId);
            return newComment;
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
    public List<Comment> listCommentsForReview(@PathVariable("reviewId") Long reviewId,@RequestParam(defaultValue = "1") Boolean mostRecent) {

        if(mostRecent!=null && mostRecent){
            return commentRepository.findByReviewId(reviewId,Sort.by("created").descending());
        }else{
            return commentRepository.findByReviewId(reviewId,Sort.by("created").ascending());
        }
    }


    /**
     * Update review document in MongoDB when new comments are added.
     *
     * @param comment The MySQL comment.
     * @return void.
     */
    private void updateCommentForReview(Comment comment,Long reviewId){

        com.udacity.course3.reviews.document.Comment mongoComment= new com.udacity.course3.reviews.document.Comment();
        mongoComment.setUserName(comment.getUserName());
        mongoComment.setUserEmail(comment.getUserEmail());
        mongoComment.setText(comment.getText());
        mongoComment.setCreated(comment.getCreated());

        Optional<Review> optionalReview = reviewMongoRepository.findById(reviewId);
        optionalReview.ifPresent(review -> {
            review.getComments().add(mongoComment);
            reviewMongoRepository.save(review);
        });
    }
}