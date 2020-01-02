package com.udacity.course3.reviews;

import com.udacity.course3.reviews.document.Comment;
import com.udacity.course3.reviews.document.Review;
import com.udacity.course3.reviews.mongoRepository.ReviewMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
public class ReviewApplicationMongoDBTests {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ReviewMongoRepository reviewMongoRepository;

    @BeforeEach
    public void dataSetup() {
        reviewMongoRepository.save(createTestReview(1L, "john", "user2@gmail.com",2,"Very slow"));
        reviewMongoRepository.save(createTestReview(2L, "tester", "mytesting@gmail.com",3,"Basic Tablet with Great Value"));
        reviewMongoRepository.save(createTestReview(3L, "tester1", "mytesting1@gmail.com",5,"Love it!"));
        reviewMongoRepository.save(createTestReview(4L, "tester2", "mytesting2@gmail.com",3,"Not as good as the last version"));
    }


    @Test
    public void testCreateReview(){
       //Testing the created reviews
        Review review = reviewMongoRepository.findById(1L).get();
        assertEquals("john",review.getUserName());
        assertEquals(2,review.getRating());
    }


    @Test
    public void testListReviewsForProduct(){
        List<Review> reviewList = reviewMongoRepository.findByProductId(1L);
        assertEquals( 4, reviewList.size());
    }

    @Test
    public void testSortedListReviewsForProduct(){
        List<Review> reviewList = reviewMongoRepository.findByProductId(1L, Sort.by("rating").descending());
        assertEquals(5,reviewList.get(0).getRating());
        assertEquals("tester1",reviewList.get(0).getUserName());
    }


    @Test
    public void testListReviewsForProductAndRating(){
        List<Review> reviewList = reviewMongoRepository.findByProductIdAndRating(1L, 3);
        assertEquals(3,reviewList.get(0).getRating());
        assertEquals(2,reviewList.size());
    }

    @Test
    public void testSaveCommentsForReview(){

        Review review = reviewMongoRepository.findById(1L).get();

        Comment comment1 = createTestComment("reviewer1", "kshan@gmail.com");
        review.getComments().add(comment1);

        Review commentedReview = reviewMongoRepository.save(review);
        assertEquals("reviewer1",commentedReview.getComments().get(0).getUserName());

    }


    private Review createTestReview(Long id, String name, String email, int rating,String headLine){
        Review review = new Review();
        review.setId(id);
        review.setUserEmail(email);
        review.setUserName(name);
        review.setRating(rating);
        review.setHeadLine(headLine);
        review.setText("I love this thing! I went back and forth between"
                +" buying this new product vs going with the older one that already had reviews to go off of.");
        review.setProductId(1L);
        return review;
    }

    private Comment createTestComment(String name, String email){
        Comment newComment = new Comment();
        newComment.setUserEmail(email);
        newComment.setUserName(name);
        newComment.setText("With Christmas quickly approaching, I'm considering purchasing for my 5 & 7 year olds");
        return newComment;
    }

}
