package com.udacity.course3.reviews;


import com.udacity.course3.reviews.mongoRepository.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ReviewMongoRepository.class)
@EnableJpaRepositories(basePackageClasses = {ReviewRepository.class, ProductRepository.class, CommentRepository.class})
public class ReviewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewsApplication.class, args);
	}




}