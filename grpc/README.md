gRPC Examples
---

# Introduction

This repository demonstrates simple examples of 
using gRPC in Spring Boot applications.

## How to Run

### Prerequisites

### Using curl to test endpoints

Ensure curl and jq are available. 

To install on linux, try this

    sudo apt  install jq

### Testing endpoints in general

- Load repository into Intellij idea.
- Run ServerApplication with idea with no arguments (uses Port 9000)
- Run ClientApplication with idea with no arguments (uses Port 8080)
- Run Postman or Curl to endpoint
- Check specific details of each example test below

### Unary Synchronous 

endpoint http://localhost:8080/author/1 

    curl http://localhost:8080/author/1 | jq '.'

    {
        "com.example.Author.author_id": 1,
        "com.example.Author.first_name": "Charles",
        "com.example.Author.last_name": "Dickens",
        "com.example.Author.gender": "Male"
    }

### Server Streaming - Asynchronous

    curl http://localhost:8080/book/1 | jq '.'

    [
      {
        "com.example.Book.book_id": 1,
        "com.example.Book.title": "Oliver Twist",
        "com.example.Book.price": 11.1,
        "com.example.Book.pages": 111,
        "com.example.Book.author_id": 1
      },
      {
        "com.example.Book.book_id": 2,
        "com.example.Book.title": "A Christmas Carol",
        "com.example.Book.price": 12.2,
        "com.example.Book.pages": 122,
        "com.example.Book.author_id": 1
      }
    ]

### Client Streaming - Asynchronous

    curl http://localhost:8080/book | jq '.'

    {
        "ExpensiveBook": {
            "com.example.Book.book_id": 6,
            "com.example.Book.title": "Mrs Dalloway>",
            "com.example.Book.price": 46.6,
            "com.example.Book.pages": 466,
            "com.example.Book.author_id": 4
        }
    }

### Bidirectional - Asynchronous

    curl http://localhost:8080/book/author/male | jq '.'

    [
        {
            "com.example.Book.book_id": 1,
            "com.example.Book.title": "Oliver Twist",
            "com.example.Book.price": 11.1,
            "com.example.Book.pages": 111,
            "com.example.Book.author_id": 1
        },
        {
            "com.example.Book.book_id": 2,
            "com.example.Book.title": "A Christmas Carol",
            "com.example.Book.price": 12.2,
            "com.example.Book.pages": 122,
            "com.example.Book.author_id": 1
        },
        {
            "com.example.Book.book_id": 3,
            "com.example.Book.title": "Hamlet",
            "com.example.Book.price": 23.3,
            "com.example.Book.pages": 233,
            "com.example.Book.author_id": 2
        }
    ]
