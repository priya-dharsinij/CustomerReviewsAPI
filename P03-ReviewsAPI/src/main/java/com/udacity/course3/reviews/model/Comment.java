package com.udacity.course3.reviews.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name="Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @Column(name = "user_name",nullable = false)
    @NotBlank
    private String userName;

    @Column(name = "user_email",nullable = false)
    @NotBlank
    private String userEmail;

    @Column(name="text")
    @NotBlank
    private String text;

    @Column(name="created",nullable = false, updatable = false)
    private Date created;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Review review;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
