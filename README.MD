# Table of contents

1. [Spring Rest Api](#spring-rest-api)
    1. [Books](#books)
        1. [Add book](#add-book)
        2. [Get books](#get-books)
        3. [Get book](#get-book)
        4. [Update book](#update-book)
        5. [Remove book](#remove-book)
    2. [Authors](#authors)
        1. [Add author](#add-author)
        2. [Get authors](#get-authors)
        3. [Get author](#get-author)
        4. [Update author](#update-author)
        5. [Remove author](#remove-author)
    3. [Genres](#genres)
        1. [Add genre](#add-genre)
        2. [Get genres](#get-genres)
        3. [Get genre](#get-genre)
        4. [Update genre](#update-genre)
        5. [Remove genre](#remove-genre)
    4. [Publishers](#publishers)
        1. [Add publisher](#add-publisher)
        2. [Get publishers](#get-publishers)
        3. [Get publisher](#get-publisher)
        4. [Update publisher](#update-publisher)
        5. [Remove publisher](#remove-publisher)
    5. [Reservations](#reservations)
        1. [Reserve book](#reserve-book)
        2. [Get reservations](#get-reservations)
        3. [Get reservation](#get-reservation)
        4. [Get member reservations](#get-member-reservations)
        5. [Get book reservations](#get-book-reservations)
        6. [Get my reservations](#get-my-reservation)
        7. [Get my reservation](#get-my-reservation)
    5. [Loans](#loans)
        1. [Borrow book](#borrow-book)
        2. [Return book](#return-book)
        3. [Get loans](#get-loans)
        4. [Get loan](#get-loan)
        5. [Get member loans](#get-member-loans)
        6. [Get book loans](#get-book-loans)
        7. [Get my loans](#get-my-loans)
        8. [Get my loan](#get-my-loan)
    7. [Ratings](#ratings)
        1. [Rate book](#rate-book)
        2. [Get average rating](#get-average-rating)
    8. [Accounts](#accounts)
        1. [Get my account](#get-my-account)
        2. [Update password](#update-password)
        3. [Update profile](#update-profile)
    9. [Members](#members)
        1. [Register member](#register-member)
        2. [Get member](#get-member)
        3. [Get members](#get-members)
    10. [Authentication](#authentication)
        1. [Authenticate](#authenticate)

# Spring Rest Api

## Books

### Add book

`POST /api/books`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "title": string,
    "description": string,
    "publicationDate": string (ISO 8601),
    "authorId": string (uuid),
    "genreId": string (uuid),
    "publisherId": string (uuid)
}
```

#### Response codes

| Code | Description |
| :--- | :---------- |
| 200  | Book added  |

### Get books

`GET /api/books`

#### Query parameters

| Parameter   | Type          | Default |
| :--------   | :---          | :------ |
| offset      | number        | 0       |
| limit       | number        | 10      |
| phrase      | string        | (none)  |
| title       | string        | (none)  |         
| description | string        | (none)  |
| authorId    | string (uuid) | (none)  |
| genreId     | string (uuid) | (none)  |
| publisherId | string (uuid) | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "67ef1817-a013-4703-b60f-d573a92d945c",
            "title": "Effective Java",
            "description: "...",
            "publicationDate": "2019-12-01"
            "author": {
                "id": "b2af04a2-8153-41ca-9e02-034d106772ed",
                "firstName": "Joshua",
                "lastName": "Blosh"
            },
            "genre": {
                "id": "2d5bee1a-840c-4d94-8f40-d3b73c8b02f7",
                "name": "Computers & Technology"
            },
            "publisher": {
                "id": "3b9deabc-e73f-4f3e-8e7d-e2e3c6d2475d",
                "name": "Addison-Wesley Professional"
            }
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get book

`GET /api/books/{bookId}`

#### Example response

```
{
    "id": "67ef1817-a013-4703-b60f-d573a92d945c",
    "title": "Effective Java",
    "description: "...",
    "publicationDate": "2019-12-01"
    "author": {
        "id": "b2af04a2-8153-41ca-9e02-034d106772ed",
        "firstName": "Joshua",
        "lastName": "Blosh"
    },
    "genre": {
        "id": "2d5bee1a-840c-4d94-8f40-d3b73c8b02f7",
        "name": "Computers & Technology"
    },
    "publisher": {
        "id": "3b9deabc-e73f-4f3e-8e7d-e2e3c6d2475d",
        "name": "Addison-Wesley Professional"
    }
}
```

### Update book

`PUT /api/books/{bookId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "title": string,
    "description": string,
    "publicationDate": string (ISO 8601),
    "authorId": string (uuid),
    "genreId": string (uuid),
    "publisherId": string (uuid)
}
```

#### Response codes

| Code | Description  |
| :--- | :----------- |
| 200  | Book updated |

### Remove book

`DELETE /api/books/{bookId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Response codes

| Code | Description  |
| :--- | :----------- |
| 204  | Book deleted |

## Authors

### Add author

`POST /api/authors`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "firstName": string,
    "lastName": string,
}
```

#### Response codes

| Code | Description   |
| :--- | :------------ |
| 200  | Author added  |

### Get authors

`GET /api/authors`

#### Query parameters

| Parameter | Type   | Default |
| :-------- | :---   | :------ |
| offset    | number | 0       |
| limit     | number | 10      |
| firstName | string | (none)  |
| lastName  | string | (none)  |
| fullName  | string | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "b2af04a2-8153-41ca-9e02-034d106772ed",
            "firstName": "Joshua",
            "lastName": "Blosh"
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get author

`GET /api/authors/{authorId}`

#### Example response

```
{
    "id": "b2af04a2-8153-41ca-9e02-034d106772ed",
    "firstName": "Joshua",
    "lastName": "Blosh"
}
```

### Update author

`PUT /api/authors/{authorId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "firstName": string,
    "lastName": string
}
```

#### Response codes

| Code | Description    |
| :--- | :------------- |
| 200  | Author updated |

### Remove author

`DELETE /api/authors/{authorId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Response codes

| Code | Description    |
| :--- | :------------- |
| 204  | Author deleted |

## Genres

### Add genre

`POST /api/genres`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "name": string,
}
```

#### Response codes

| Code | Description |
| :--- | :---------- |
| 200  | Genre added |

### Get genres

`GET /api/genres`

#### Query parameters

| Parameter | Type   | Default |
| :-------- | :---   | :------ |
| offset    | number | 0       |
| limit     | number | 10      |
| name      | string | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "2d5bee1a-840c-4d94-8f40-d3b73c8b02f7",
            "name": "Computers & Technology"
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get genre

`GET /api/genres/{genreId}`

#### Example response

```
{
    "id": "2d5bee1a-840c-4d94-8f40-d3b73c8b02f7",
    "name": "Computers & Technology"
}
```

### Update genre

`PUT /api/genres/{genreId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "name": string,
}
```

#### Response codes

| Code | Description   |
| :--- | :------------ |
| 200  | Genre updated |

### Remove genre

`DELETE /api/genres/{genreId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Response codes

| Code | Description   |
| :--- | :------------ |
| 204  | Genre deleted |

## Publishers

### Add publisher

`POST /api/publishers`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "name": string,
}
```

#### Response codes

| Code | Description     |
| :--- | :-------------- |
| 200  | Publisher added |

### Get publishers

`GET /api/publishers`

#### Query parameters

| Parameter | Type   | Default |
| :-------- | :---   | :------ |
| offset    | number | 0       |
| limit     | number | 10      |
| name      | string | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "3b9deabc-e73f-4f3e-8e7d-e2e3c6d2475d",
            "name": "Addison-Wesley Professional"
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get publisher

`GET /api/publishers/{publisherId}`

#### Example response

```
{
    "id": "3b9deabc-e73f-4f3e-8e7d-e2e3c6d2475d",
    "name": "Addison-Wesley Professional"
}
```

### Update publisher

`PUT /api/publishers/{publisherId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "name": string,
}
```

#### Response codes

| Code | Description       |
| :--- | :---------------- |
| 200  | Publisher updated |

### Remove publisher

`DELETE /api/publishers/{publisherId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Response codes

| Code | Description       |
| :--- | :---------------- |
| 204  | Publisher deleted |

## Reservations

### Reserve book

`POST /api/books/{bookId}/reservations`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |


#### Response codes

| Code | Description   |
| :--- | :------------ |
| 200  | Book reserved |

### Get reservations

`GET /api/reservations`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter | Type   | Default |
| :-------- | :---   | :------ |
| offset    | number | 0       |
| limit     | number | 10      |
| status    | string | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "1c642ca4-25ca-479b-857b-e2a093fe720c",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "events": [
                {
                    "status": "SUBMITTED",
                    "occurrenceDate": "2020-01-01T02:00+02:00"
                }
            ]
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get reservation

`GET /api/reservation/{reservationId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Example response

```
{
    "id": "1c642ca4-25ca-479b-857b-e2a093fe720c",
    "member": {
        "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
        "firstName": "James",
        "lastName": "Smith"
    }, {
        "id": "67ef1817-a013-4703-b60f-d573a92d945c",
        "title": "Effective Java",
    },
    "events": [
        {
            "status": "SUBMITTED",
            "occurrenceDate": "2020-01-01T02:00+02:00"
         }
    ]
}
```

### Get member reservations

`GET /api/member/{memberId}/reservations`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter | Type   | Default |
| :-------- | :---   | :------ |
| offset    | number | 0       |
| limit     | number | 10      |
| status    | string | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "1c642ca4-25ca-479b-857b-e2a093fe720c",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "events": [
                {
                    "status": "SUBMITTED",
                    "occurrenceDate": "2020-01-01T02:00+02:00"
                }
            ]
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get book reservations

`GET /api/books/{bookId}/reservations`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter | Type   | Default |
| :-------- | :---   | :------ |
| offset    | number | 0       |
| limit     | number | 10      |
| status    | string | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "1c642ca4-25ca-479b-857b-e2a093fe720c",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "events": [
                {
                    "status": "SUBMITTED",
                    "occurrenceDate": "2020-01-01T02:00+02:00"
                }
            ]
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get my reservations

`GET /api/member/reservations`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter | Type   | Default |
| :-------- | :---   | :------ |
| offset    | number | 0       |
| limit     | number | 10      |
| status    | string | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "1c642ca4-25ca-479b-857b-e2a093fe720c",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "events": [
                {
                    "status": "SUBMITTED",
                    "occurrenceDate": "2020-01-01T02:00+02:00"
                }
            ]
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get my reservation

`GET /api/member/reservations/{reservationId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Example response

```
{
    "id": "1c642ca4-25ca-479b-857b-e2a093fe720c",
    "member": {
        "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
        "firstName": "James",
        "lastName": "Smith"
    }, {
        "id": "67ef1817-a013-4703-b60f-d573a92d945c",
        "title": "Effective Java",
    },
    "events": [
        {
            "status": "SUBMITTED",
            "occurrenceDate": "2020-01-01T02:00+02:00"
        }
    ]
}
```

## Loans

### Borrow book

`POST /api/books/{bookId}/loans`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |


#### Response codes

| Code | Description   |
| :--- | :------------ |
| 200  | Book borrowed |

### Return book

`PUT /api/books/{bookId}/loans`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |


#### Response codes

| Code | Description   |
| :--- | :------------ |
| 200  | Book returned |

### Get loans

`GET /api/loans`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter   | Type              | Default |
| :---------- | :---------------- | :------ |
| offset      | number            | 0       |
| limit       | number            | 10      |
| minLoanDate | string (ISO 8601) | (none)  |
| maxLoanDate | string (ISO 8601) | (none)  |
| minDueDate  | string (ISO 8601) | (none)  |
| maxDueDate  | string (ISO 8601) | (none)  |
| returned    | boolean           | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "e023bfd3-beb0-436e-bbaf-189bd88508cd",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "loanDate": "2020-05-01T02:00+02:00",
            "dueDate": "2020-10-01T02:00+02:00",
            "returned": false
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get loan

`GET /api/loans/{reservationId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Example response

```
{
    "id": "e023bfd3-beb0-436e-bbaf-189bd88508cd",
    "member": {
        "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
        "firstName": "James",
        "lastName": "Smith"
    }, {
        "id": "67ef1817-a013-4703-b60f-d573a92d945c",
        "title": "Effective Java",
    },
    "loanDate": "2020-05-01T02:00+02:00",
    "dueDate": "2020-10-01T02:00+02:00",
    "returned": false
}
```

### Get member loans

`GET /api/member/{memberId}/loans`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter   | Type              | Default |
| :---------- | :---------------- | :------ |
| offset      | number            | 0       |
| limit       | number            | 10      |
| minLoanDate | string (ISO 8601) | (none)  |
| maxLoanDate | string (ISO 8601) | (none)  |
| minDueDate  | string (ISO 8601) | (none)  |
| maxDueDate  | string (ISO 8601) | (none)  |
| returned    | boolean           | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "e023bfd3-beb0-436e-bbaf-189bd88508cd",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "loanDate": "2020-05-01T02:00+02:00",
            "dueDate": "2020-10-01T02:00+02:00",
            "returned": false
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get book loans

`GET /api/books/{bookId}/loans`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter   | Type              | Default |
| :---------- | :---------------- | :------ |
| offset      | number            | 0       |
| limit       | number            | 10      |
| minLoanDate | string (ISO 8601) | (none)  |
| maxLoanDate | string (ISO 8601) | (none)  |
| minDueDate  | string (ISO 8601) | (none)  |
| maxDueDate  | string (ISO 8601) | (none)  |
| returned    | boolean           | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "e023bfd3-beb0-436e-bbaf-189bd88508cd",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "loanDate": "2020-05-01T02:00+02:00",
            "dueDate": "2020-10-01T02:00+02:00",
            "returned": false
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get my loans

`GET /api/member/loans`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Query parameters

| Parameter   | Type              | Default |
| :---------- | :---------------- | :------ |
| offset      | number            | 0       |
| limit       | number            | 10      |
| minLoanDate | string (ISO 8601) | (none)  |
| maxLoanDate | string (ISO 8601) | (none)  |
| minDueDate  | string (ISO 8601) | (none)  |
| maxDueDate  | string (ISO 8601) | (none)  |
| returned    | boolean           | (none)  |

#### Example response

```
{
    "elements": [
        {
            "id": "e023bfd3-beb0-436e-bbaf-189bd88508cd",
            "member": {
                "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
                "firstName": "James",
                "lastName": "Smith"
            }, {
                "id": "67ef1817-a013-4703-b60f-d573a92d945c",
                "title": "Effective Java",
            },
            "loanDate": "2020-05-01T02:00+02:00",
            "dueDate": "2020-10-01T02:00+02:00",
            "returned": false
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

### Get my loan

`GET /api/member/loans/{loanId}`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Authorization | Bearer {token}   |

#### Example response

```
{
    "id": "e023bfd3-beb0-436e-bbaf-189bd88508cd",
    "member": {
        "id": "8fdf6301-4249-4508-b246-b18586c2fd63",
        "firstName": "James",
        "lastName": "Smith"
    }, {
        "id": "67ef1817-a013-4703-b60f-d573a92d945c",
        "title": "Effective Java",
    },
    "loanDate": "2020-05-01T02:00+02:00",
    "dueDate": "2020-10-01T02:00+02:00",
    "returned": false
}
```

## Ratings

### Rate book

`POST /api/books/{bookId}/ratings`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "value": number (0 - 5)
}
```

#### Response codes

| Code | Description |
| :--- | :---------- |
| 200  | Book rated  |

### Get average rating

`GET /api/books/{bookId}/ratings/average`

#### Example response

```
{
    "value": 4.5
}
```

## Accounts

### Get my account

`GET /api/account`

#### Headers

| Name          | Value          |
| :------------ | :------------- |
| Authorization | Bearer {token} |

#### Example response

```
{
    "username": "user1",
    "profile": {
        "firstName": "James",
        "lastName": "Smith"
    }
}
```

### Update password

`PUT /api/account/password`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "password": string
}
```

#### Response codes

| Code | Description      |
| :--- | :--------------- |
| 200  | Password updated |

### Update profile

`PUT /api/account/profile`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |
| Authorization | Bearer {token}   |

#### Body

```
{
    "firstName": "James",
    "lastName": "Smith"
}
```

#### Response codes

| Code | Description     |
| :--- | :-------------- |
| 200  | Profile updated |

## Members

### Register member

`POST /api/members`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |

#### Body

```
{
    "username": string,
    "password": string,
    "firstName": string,
    "lastName": string,
}
```

#### Response codes

| Code | Description       |
| :--- | :---------------- |
| 200  | Member registered |

### Get member

`GET /api/members/{memberId}`

#### Headers

| Name          | Value          |
| :------------ | :------------- |
| Authorization | Bearer {token} |

#### Example response

```
{
    "id": "cd739532-61ce-4eb4-ab6a-9864e2e350b9",
    "account": {
        "username": "user1",
        "profile": {
            "firstName": "James",
            "lastName": "Smith"
        }
    }
}
```

### Get members

`GET /api/members`

#### Headers

| Name          | Value          |
| :------------ | :------------- |
| Authorization | Bearer {token} |

#### Example response

```
{
    "elements": [
        {
            "id": "cd739532-61ce-4eb4-ab6a-9864e2e350b9",
            "account": {
                "username": "user1",
                "profile": {
                    "firstName": "James",
                    "lastName": "Smith"
                }
            }
        }
    ],
    "metadata": {
        "offset": 0,
        "limit": 0,
        "total": 1
    }
}
```

## Authentication

### Authenticate

`POST /api/auth`

#### Headers

| Name          | Value            |
| :------------ | :--------------- |
| Content-Type  | application/json |

#### Body

```
{
    "username": string,
    "password": string
}
```

#### Response codes

| Code | Description   |
| :--- | :------------ |
| 200  | Authenticated |

