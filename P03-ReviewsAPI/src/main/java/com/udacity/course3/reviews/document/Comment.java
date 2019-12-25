package com.udacity.course3.reviews.document;

import javax.validation.constraints.NotBlank;
import java.util.Date;


public class Comment {

    @NotBlank
    private String userName;
    @NotBlank
    private String userEmail;
    @NotBlank
    private String text;
    private Date created;

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

}

