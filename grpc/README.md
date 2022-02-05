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

- Load repository into Intellij.
- Run ServerApplication with idea with no arguments (Port 9000)
- Run ClientApplication with idea with no arguments (Port 8080)
- Run Postman or Curl to endpoint
- Check specific details of each example test below

### Synchronous endpoint http://localhost:8080/author/1 

    curl http://localhost:8080/author/1 | jq '.'

    {
        "com.example.Author.author_id": 1,
        "com.example.Author.first_name": "Charles",
        "com.example.Author.last_name": "Dickens",
        "com.example.Author.gender": "Male"
    }

