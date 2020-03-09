CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE account
(
    id         INT8         NOT NULL,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE author
(
    id         INT8         NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE book
(
    id               INT8         NOT NULL,
    average_rating   FLOAT8,
    description      VARCHAR(255) NOT NULL,
    loaned           BOOLEAN      NOT NULL,
    publication_date DATE         NOT NULL,
    title            VARCHAR(255) NOT NULL,
    author_id        INT8         NOT NULL,
    genre_id         INT8         NOT NULL,
    publisher_id     INT8         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE genre
(
    id   INT8         NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE librarian
(
    id         INT8 NOT NULL,
    account_id INT8 NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE loan
(
    id            INT8    NOT NULL,
    complete_date TIMESTAMP,
    completed     BOOLEAN NOT NULL,
    due_date      TIMESTAMP,
    loan_date     TIMESTAMP,
    book_id       INT8    NOT NULL,
    member_id     INT8    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE member
(
    id         INT8 NOT NULL,
    account_id INT8 NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE publisher
(
    id   INT8         NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE rating
(
    id        INT8 NOT NULL,
    rate_date TIMESTAMP,
    value     INT4 NOT NULL,
    book_id   INT8 NOT NULL,
    member_id INT8 NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id            INT8      NOT NULL,
    cancel_date   TIMESTAMP,
    complete_date TIMESTAMP,
    duration      INT8      NOT NULL,
    prepare_date  TIMESTAMP,
    reject_date   TIMESTAMP,
    status        VARCHAR(255),
    submit_date   TIMESTAMP NOT NULL,
    book_id       INT8      NOT NULL,
    member_id     INT8      NOT NULL,
    index         INT4,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS librarian
    ADD CONSTRAINT UK_cwa78i1xypcqis1soabh8n247 UNIQUE (account_id);

ALTER TABLE IF EXISTS book
    ADD CONSTRAINT FKklnrv3weler2ftkweewlky958
        FOREIGN KEY (author_id)
            REFERENCES author;

ALTER TABLE IF EXISTS book
    ADD CONSTRAINT FKm1t3yvw5i7olwdf32cwuul7ta
        FOREIGN KEY (genre_id)
            REFERENCES genre;

ALTER TABLE IF EXISTS book
    ADD CONSTRAINT FKgtvt7p649s4x80y6f4842pnfq
        FOREIGN KEY (publisher_id)
            REFERENCES publisher;

ALTER TABLE IF EXISTS librarian
    ADD CONSTRAINT FKadwpuo34i30fx2l2lol7tx243
        FOREIGN KEY (account_id)
            REFERENCES account;

ALTER TABLE IF EXISTS loan
    ADD CONSTRAINT FK88c0ydlo57pcgp137tntrgqx1
        FOREIGN KEY (book_id)
            REFERENCES book;

ALTER TABLE IF EXISTS loan
    ADD CONSTRAINT FKskvlv9lnj3plivn0grmqf950f
        FOREIGN KEY (member_id)
            REFERENCES member;

ALTER TABLE IF EXISTS member
    ADD CONSTRAINT FK4jsivcqa7rxm6w59nggnpywh9
        FOREIGN KEY (account_id)
            REFERENCES account;

ALTER TABLE IF EXISTS rating
    ADD CONSTRAINT FK7y1acs6la7vkgb5ulm44729sc
        FOREIGN KEY (book_id)
            REFERENCES book;

ALTER TABLE IF EXISTS rating
    ADD CONSTRAINT FKe14in1y4sugqskjl9u4m2o18x
        FOREIGN KEY (member_id)
            REFERENCES member;

ALTER TABLE IF EXISTS reservation
    ADD CONSTRAINT FKirxtcw4s6lhwi6l9ocrk6bjfy
        FOREIGN KEY (book_id)
            REFERENCES book;

ALTER TABLE IF EXISTS reservation
    ADD CONSTRAINT FK68999qe28ym9eqqlowybh9nvn
        FOREIGN KEY (member_id)
            REFERENCES member;
