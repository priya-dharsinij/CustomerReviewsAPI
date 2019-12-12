CREATE TABLE Product (
    product_id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    price DECIMAL(4,2) NOT NULL,
    ratings decimal(2,1) DEFAULT 0,
    PRIMARY KEY (product_id)
);


CREATE TABLE Review (
    review_id int NOT NULL AUTO_INCREMENT,
    rating int DEFAULT 0,
    user_name varchar(100) NOT NULL,
    user_email varchar(100) NOT NULL,
    head_line varchar(100) NOT NULL,
    text varchar(1000) NOT NULL,
    created TIMESTAMP NOT NULL,
    product_id int,
    PRIMARY KEY (review_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Comment (
    comment_id int NOT NULL AUTO_INCREMENT,
    user_name varchar(100) NOT NULL,
    user_email varchar(100) NOT NULL,
    text varchar(1000) NOT NULL,
    created TIMESTAMP NOT NULL,
    review_id int,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (review_id) REFERENCES Review(review_id)
);