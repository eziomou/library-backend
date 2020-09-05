CREATE TABLE IF NOT EXISTS account
(
    id         uuid         NOT NULL,
    username   varchar(255) NOT NULL,
    password   varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name  varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS member
(
    id         uuid NOT NULL,
    account_id uuid NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_member_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS librarian
(
    id         uuid NOT NULL,
    account_id uuid NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_librarian_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS author
(
    id         uuid         NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name  varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genre
(
    id   uuid         NOT NULL,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS publisher
(
    id   uuid         NOT NULL,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS book
(
    id               uuid                     NOT NULL,
    title            varchar(255)             NOT NULL,
    description      varchar(8000)            NOT NULL,
    publication_date timestamp with time zone NOT NULL,
    author_id        uuid                     NOT NULL,
    genre_id         uuid                     NOT NULL,
    publisher_id     uuid                     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES author (id),
    CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genre (id),
    CONSTRAINT fk_publisher FOREIGN KEY (publisher_id) REFERENCES publisher (id)
);

CREATE TABLE IF NOT EXISTS loan
(
    id        uuid                     NOT NULL,
    member_id uuid                     NOT NULL,
    book_id   uuid                     NOT NULL,
    loan_date timestamp with time zone NOT NULL,
    due_date  timestamp with time zone NOT NULL,
    returned  bool                     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_loan_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES book (id)
);

CREATE TABLE IF NOT EXISTS reservation
(
    id        uuid NOT NULL,
    member_id uuid NOT NULL,
    book_id   uuid NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES member (id),
    CONSTRAINT fk_reservation_book FOREIGN KEY (book_id) REFERENCES book (id)
);

CREATE TABLE IF NOT EXISTS reservation_event
(
    id                 uuid                     NOT NULL,
    reservation_id     uuid                     NOT NULL,
    reservation_status varchar(255)             NOT NULL,
    occurrence_date    timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_reservation_event_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rating
(
    id        uuid                     NOT NULL,
    member_id uuid                     NOT NULL,
    book_id   uuid                     NOT NULL,
    value     int                      NOT NULL,
    rate_date timestamp with time zone NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_rating_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT fk_rating_book FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE
);