package com.udacity.course3.reviews;


import com.udacity.course3.reviews.model.Comment;
import com.udacity.course3.reviews.model.Product;
import com.udacity.course3.reviews.model.Review;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureDataMongo
public class ReviewsApplicationTests {

	@Autowired
	EntityManager entityManager;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	CommentRepository commentRepository;


	@Test
	public void injectedComponentsAreNotNull(){
		assertThat(entityManager).isNotNull();
		assertThat(productRepository).isNotNull();
		assertThat(reviewRepository).isNotNull();
		assertThat(commentRepository).isNotNull();
	}

	@Test
	public void testSaveProduct(){
		Product newProduct = createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),4);
		entityManager.persist(newProduct);

		Optional<Product> actual = productRepository.findById(newProduct.getId());
		actual.ifPresent(value -> assertEquals(newProduct.getId(),value.getId()));
	}

	@Test
	public void testListProducts(){
		Product product1 =  createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),5);
		Product product2 =  createTestProduct("Echo Dot 3", BigDecimal.valueOf(25.00),4);

		entityManager.persist(product1);
		entityManager.persist(product2);

		Iterable<Product> all = productRepository.findAll();
		List<Product> productList = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
		assertEquals(2,productList.size());
	}

	@Test
	public void testCreateReviewForProduct(){

		Product newProduct = createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),4);
		entityManager.persist(newProduct);

		Review newReview = createTestReview(newProduct, "tester", "mytesting@gmail.com",5,"Love it!");
		entityManager.persist(newReview);

		Optional<Review> actual = reviewRepository.findById(newReview.getId());
		actual.ifPresent(value -> assertEquals(newReview.getId(),value.getId()));
	}

	@Test
	public void testListReviewsForProduct(){

		Product newProduct = createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),4);
		entityManager.persist(newProduct);


		entityManager.persist(createTestReview(newProduct, "tester", "mytesting@gmail.com",5,"Basic Tablet with Great Value"));
		entityManager.persist(createTestReview(newProduct, "john", "user2@gmail.com",2,"Very slow"));

		List<Review> reviewList = reviewRepository.findByProductId(newProduct.getId());
		assertEquals("tester",reviewList.get(0).getUserName());
		assertEquals("john",reviewList.get(1).getUserName());
	}

	@Test
	public void testSortedListReviewsForProduct(){

		Product newProduct = createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),4);
		entityManager.persist(newProduct);


		entityManager.persist(createTestReview(newProduct, "john", "user2@gmail.com",2,"Very slow"));
		entityManager.persist(createTestReview(newProduct, "tester", "mytesting@gmail.com",5,"Basic Tablet with Great Value"));


		List<Review> reviewList = reviewRepository.findByProductId(newProduct.getId(), Sort.by("rating").descending());
		assertEquals(5,reviewList.get(0).getRating());
		assertEquals("john",reviewList.get(1).getUserName());
	}

	@Test
	public void testListReviewsForProductAndRating(){

		Product newProduct = createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),4);
		entityManager.persist(newProduct);


		entityManager.persist(createTestReview(newProduct, "john", "user2@gmail.com",2,"Very slow"));
		entityManager.persist(createTestReview(newProduct, "tester", "mytesting@gmail.com",3,"Basic Tablet with Great Value"));
		entityManager.persist(createTestReview(newProduct, "tester1", "mytesting1@gmail.com",5,"Love it!"));
		entityManager.persist(createTestReview(newProduct, "tester2", "mytesting2@gmail.com",3,"Not as good as the last version"));

		List<Review> reviewList = reviewRepository.findByProductIdAndRating(newProduct.getId(), 3);
		assertEquals(3,reviewList.get(0).getRating());
		assertEquals(2,reviewList.size());
	}



	@Test
	public void testCreateCommentForReview(){

		Product newProduct = createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),5);
		entityManager.persist(newProduct);

		Review newReview = createTestReview(newProduct, "tester", "mytesting@gmail.com",5,"Love it!");
		entityManager.persist(newReview);

		Comment newComment = createTestComment(newReview,"reviewer1","kshan@gmail.com");
		entityManager.persist(newComment);

		Optional<Comment> actual = commentRepository.findById(newComment.getId());
		actual.ifPresent(value -> assertEquals(newComment.getId(),value.getId()));
	}

	@Test
	public void testListCommentsForReview(){
		Product newProduct = createTestProduct("All-New Fire HD 8", BigDecimal.valueOf(40.00),5);
		entityManager.persist(newProduct);

		Review newReview = createTestReview(newProduct, "tester", "mytesting@gmail.com",5,"Love it!");
		entityManager.persist(newReview);

		entityManager.persist(createTestComment(newReview,"reviewer1","kshan@gmail.com"));
		entityManager.persist(createTestComment(newReview,"reviewer2","jane23@gmail.com"));
		entityManager.persist(createTestComment(newReview,"reviewer3","shalini@gmail.com"));


		List<Comment> commentList = commentRepository.findByReviewId(newReview.getId(),Sort.by("created").descending());
		assertEquals("reviewer3",commentList.get(0).getUserName());

	}



	private Product createTestProduct(String name, BigDecimal price,float ratings){
		Product newProduct = new Product();
		newProduct.setName(name);
		newProduct.setPrice(price);
		newProduct.setRatings(ratings);
		return newProduct;
	}

	private Review createTestReview(Product product, String name, String email, int rating,String headLine){
		Review review = new Review();
		review.setUserEmail(email);
		review.setUserName(name);
		review.setRating(rating);
		review.setHeadLine(headLine);
		review.setText("I love this thing! I went back and forth between"
				+" buying this new product vs going with the older one that already had reviews to go off of.");
		review.setProduct(product);
		return review;
	}

	private Comment createTestComment(Review review, String name, String email){
		Comment newComment = new Comment();
		newComment.setReview(review);
		newComment.setUserEmail(email);
		newComment.setUserName(name);
		newComment.setText("With Christmas quickly approaching, I'm considering purchasing for my 5 & 7 year olds");
		return newComment;
	}




}