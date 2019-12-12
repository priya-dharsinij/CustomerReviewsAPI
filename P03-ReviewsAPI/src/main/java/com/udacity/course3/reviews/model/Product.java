package com.udacity.course3.reviews.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="product_id")
    private Long id;

    @Column(name="name",nullable = false)
    @NotBlank
    private String name;

    @Column(name="price",nullable = true)
    @NotNull
    private BigDecimal price;

    @Column(name="ratings")
    private float ratings;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "product")
    private List<Review> reviews = new ArrayList();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
