# library-backend



## Table of contents
1. [Overview](#overview)
2. [Architecture](#architecture)
    1. [UI](#ui)
    2. [Application](#application)
    3. [Domain](#domain)
    4. [Infrastructure](#infrastructure)
3. [Setup](#setup)
    1. [Configuration](#configuration)
    2. [Run](#run)
    3. [Database](#database)
4. [REST API](#rest-api)
    1. [Books](#books)
        1. [Create a book](#create-a-book)
        2. [Get a book](#get-a-book)
        3. [Get books](#get-books)
        4. [Update a book](#update-a-book)
        5. [Delete a book](#delete-a-book)
    2. [Authors](#authors)
        1. [Create an author](#create-an-author)
        2. [Get an author](#get-an-author)
        3. [Get author](#get-authors)
        4. [Update an author](#update-an-author)
        5. [Delete an author](#delete-an-author)
    3. [Genres](#genres)
        1. [Create a genre](#create-a-genre)
        2. [Get a genre](#get-a-genre)
        3. [Get genres](#get-genres)
        4. [Update a genre](#update-a-genre)
        5. [Delete a genre](#delete-a-genre)
    4. [Publishers](#publishers)
        1. [Create a publisher](#create-a-publisher)
        2. [Get a publisher](#get-a-publisher)
        3. [Get publishers](#get-publishers)
        4. [Update a publisher](#update-a-publisher)
        5. [Delete a publisher](#delete-a-publisher)
    5. [Reservations](#reservations)
        1. [Create a reservation](#create-a-reservation)
        2. [Get a reservation](#get-a-reservation)
        3. [Get reservations](#get-reservations)
        4. [Update a reservation](#update-a-reservation)
        5. [Delete a reservation](#delete-a-reservation)
    6. [Loans](#loans)
        1. [Create a loan](#create-a-loan)
        2. [Get a loan](#get-a-loan)
        3. [Get loans](#get-loans)
        4. [Update a loan](#update-a-loan)
        5. [Delete a loan](#delete-a-loan)
    7. [Ratings](#ratings)
        1. [Create a rating](#create-a-rating)
        2. [Get a rating](#get-a-rating)
        3. [Get ratings](#get-ratings)
        4. [Update a rating](#update-a-rating)
        5. [Delete a rating](#delete-a-rating)
    8. [Accounts](#accounts)
        1. [Get an account](#get-an-account)
        2. [Update an account](#update-an-account)
    9. [Members](#members)
        1. [Create a member](#create-a-member)
        2. [Get a member](#get-a-member)
        3. [Get members](#get-members)
    10. [Authentication](#authentication)
        1. [Authenticate](#authenticate)



## Overview

This is a backend version of the library management system that provides a REST API. The library-frontend application is a React application which consumes the REST API.



## Architecture

The application is based on Domain Driven Design (DDD) architecture and consists of four primary layers.


### UI

This layer contains controllers that define all RESTful methods. Each controller uses the application services defined in the layer below.


### Application

The main purpose of this layer is to manage domain objects defined in the layer below to perform specific tasks. This layer contains application-specific logic that is not associated with business logic.


### Domain

This layer is the heart of the application, all critical business logic is defined here.

This layer is made of basic DDD building blocks:
* Entities,
* Value objects,
* Aggregate roots,
* Repositories,
* Factories,
* Services (domain services).


### Infrastructure

This layer supports communication between the above layers and provides access to the database and system resources.



## Setup


### Configuration

To configure application use `application.properties` located in:

```
project_root/src/main/resources
```


### Run

```
cd project_root
mvn spring-boot:run
```

or

```
cd project_root
mvn clean package
cd target
java -jar library-backend-1.0-SNAPSHOT.jar
```

### Database

To create a database you can use `schema.sql` or allow Hibernate to automatically generate DDL.

To fill the database you can use `data.sql` which contains test data.

To create a librarian (administrator) you will need the following insert:

`INSERT INTO librarian (id, account_id) VALUES (NEXTVAL('hibernate_sequence'), your_account_id);`


## REST API


### Books

#### Create a book

```
POST /api/books
 
Content-Type: application/json
Authorization: Bearer {token}

{
    "title": string,
    "authorId": number,
    "genreId": number,
    "publisherId": number,
    "description": string,
    "publicationDate": string (ISO 8601)
}
```

When succeed returns **200** status code and the created book.

#### Get a book

```
GET /api/books/{id}
```

When succeed returns **200** status code and the found author.

#### Get books

```
GET /api/books

URL parameters:
• title: string | null
• authorId: number | null
• authorFullName: string | null
• genreId: number | null
• genreName: string | null
• publisherId: number | null
• publisherName: string | null
• page: number | null
```

When succeed returns **200** status code and the found books.

#### Update a book

```
PUT /api/books/{id}

Content-Type: application/json
Authorization: Bearer {token}
    
{
    "title": string | null,
    "authorId": number | null,
    "genreId": number | null,
    "publisherId": number | null,
    "description": string | null,
    "publicationDate": string (ISO 8601) | null
}
```

When succeed returns **200** status code and the updated book.

#### Delete a book

```
DELETE /api/books/{id}

Authorization: Bearer {token}
```

When succeed returns **201** status code.


### Authors

#### Create an author

```
POST /api/author
 
Content-Type: application/json
Authorization: Bearer {token}
    
{
    "firstName": string,
    "lastName": string
}
```

When succeed returns **200** status code and the created author.

#### Get an author

```
GET /api/authors/{id}
```

When succeed returns **200** status code and the found author.

#### Get authors

```
GET /api/authors

URL parameters:
• firstName: string | null
• lastName: string | null
• fullName: string | null
• page: number | null
```

When succeed returns **200** status code and the found authors.

#### Update an author

```
PUT /api/authors/{id}

Content-Type: application/json
Authorization: Bearer {token}
    
{
    "firstName": string | null,
    "lastName": string | null
}
```

When succeed returns **200** status code and the updated author.

#### Delete an author

```
DELETE /api/authors/{id}

Authorization: Bearer {token}
```

When succeed return **201** status code.


### Genres

#### Create a genre

```
POST /api/genres

Content-Type: application/json
Authorization: Bearer {token}

{
    "name": string
}
```

When succeed returns **200** status code and the created book.

#### Get a genre

```
GET /api/genres/{id}
```

When succeed returns **200** status code and the found genre.

#### Get genres

```
GET /api/genres

URL parameters:
• name: string | null
• page: number | null
```

When succeed returns **200** status code and the found genres.

#### Update a genre

```
PUT /api/genres/{id}

Content-Type: application/json
Authorization: Bearer {token}

{
    "name": string | null
}
```

When succeed returns **200** status code and the updated genre.

#### Delete a genre

```
DELETE /api/genres/{id}

Authorization: Bearer {token}
```

When succeed returns **201** status code.


### Publishers

#### Create a publisher

```
POST /api/publishers

Content-Type: application/json
Authorization: Bearer {token}

{
    "name": string
}
```

When succeed returns **200** status code and the created publisher.

#### Get a publisher

```
GET /api/publishers/{id}
```

When succeed returns **200** status code and the found publisher.

#### Get publishers

```
GET /api/publishers

URL parameters:
• name: string | null
• page: number | null
```

When succeed returns **200** status code and the found publishers.

#### Update a publisher

```
PUT /api/publishers/{id}

Content-Type: application/json
Authorization: Bearer {token}

{
    "name": string | null
}
```

When succeed returns **200** status code and the updated publisher.

#### Delete a publisher

```
Authorization: Bearer {token}

DELETE /api/publishers/{id}
```

When succeed returns **201** status code.


### Reservations

#### Create a reservation

```
POST /api/reservations

Content-Type: application/json
Authorization: Bearer {token}

{
    "bookId": number,
    "duration": string (ISO 8601)
}
```

When succeed returns **200** status code and the created reservation.

#### Get a reservation

```
GET /api/reservations/{id}

Authorization: Bearer {token}
```

When succeed returns **200** status code and the found reservation.

#### Get reservations

```
GET /api/reservations

Authorization: Bearer {token}

URL parameters:
• memberUsername: string | null
• bookId: number | null
• status: "SUBMITTED" | "PREPARED" | "COMPLETED" | "CANCELLED" | "REJECTED" | null
• page: number | null
```

When succeed returns **200** status code and the found reservations.

#### Update a reservation

```
PUT /api/reservations/{id}

Content-Type: application/json
Authorization: Bearer {token}

{
    "status": "CANCELLED" | "REJECTED" | null,
    "duration": string (ISO 8601) | null
}
```

When succeed returns **200** status code and the updated reservation.

#### Delete a reservation

```
DELETE /api/reservations/{id}

Authorization: Bearer {token}
```

When succeed returns **201** status code.


### Loans

#### Create a loan

```
POST /api/loans

Content-Type: application/json
Authorization: Bearer {token}

{
    "bookId": number
}
```

When succeed returns **200** status code and the created loan.

#### Get a loan

```
GET /api/loans/{id}

Authorization: Bearer {token}
```

When succeed returns **200** status code and the found loan.

#### Get loans

```
GET /api/loans

Authorization: Bearer {token}

URL parameters:
• memberUsername: string | null
• bookId: number | null
• completed: boolean | null
• page: number | null
```

When succeed returns **200** status code and the found loans.

#### Update a loan

```
PUT /api/loans/{id}

Content-Type: application/json
Authorization: Bearer {token}

{
    "completed": boolean | null
}
```

When succeed returns **200** status code and the updated loan.

#### Delete a loan

```
DELETE /api/loans/{id}

Authorization: Bearer {token}
```

When succeed returns **201** status code.


### Ratings

#### Create a rating

```
POST /api/ratings

Content-Type: application/json
Authorization: Bearer {token}

{
    "bookId": number,
    "value": number
}
```

When succeed returns **200** status code and the created rating.

#### Get a rating

```
GET /api/ratings/{id}
```

When succeed returns **200** status code and the found rating.

#### Get ratings

```
GET /api/ratings

URL parameters:
• memberUsername: string | null
• bookId: number | null
• page: number | null
```

When succeed returns **200** status code and the found ratings.

#### Update a rating

```
PUT /api/ratings/{id}

Content-Type: application/json
Authorization: Bearer {token}

{
    "value": number | null
}
```

When succeed returns **200** status code and the updated rating.

#### Delete a rating

```
DELETE /api/ratings/{id}

Authorization: Bearer {token}
```

When succeed returns **201** status code.


### Accounts


#### Get an account

```
GET /api/accounts/{id}

Authorization: Bearer {token}
```

When succeed returns **200** status code and the found account.

#### Update an account

```
PUT /api/accounts/{id}

Content-Type: application/json
Authorization: Bearer {token}

{
    "password": string | null,
    "firstName": string | null,
    "lastName": string | null
}
```

When succeed returns **200** status code and the updated account.

### Members

#### Create a member

```
POST /api/members

Content-Type: application/json

{
    "username": string,
    "password": string,
    "firstName": string,
    "lastName": string
}
```

When succeed returns **200** status code and the created member.

#### Get a member

```
GET /api/members/{username}

Content-Type: application/json
Authorization: Bearer {token}
```

When succeed returns **200** status code and the found member.

#### Get members

```
GET /api/members

Content-Type: application/json
Authorization: Bearer {token}

URL parameters:
• username: string | null
• firstName: string | null
• lastName: string | null
• fullName: string | null
• page: number | null
```

When succeed returns **200** status code and the found members.

### Authentication

#### Authenticate

```
POST /api/auth

Content-Type: application/json

{
    "username": string,
    "password": string
}
```

When succeed returns **200** status code and the token.

