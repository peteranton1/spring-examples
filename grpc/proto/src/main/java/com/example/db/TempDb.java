package com.example.db;

import com.example.Author;
import com.example.Book;

import java.util.ArrayList;
import java.util.List;

public class TempDb {

  public static List<Author> getAuthorsFromTempDb() {
    return new ArrayList<>() {
      {
        add(Author.newBuilder()
            .setAuthorId(1)
            .setFirstName("Charles")
            .setLastName("Dickens")
            .setGender("Male")
            .build());
        add(Author.newBuilder()
            .setAuthorId(2)
            .setFirstName("William")
            .setLastName("Shakespeare")
            .setGender("Male")
            .build());
        add(Author.newBuilder()
            .setAuthorId(3)
            .setFirstName("JK")
            .setLastName("Rowlings")
            .setGender("Female")
            .build());
        add(Author.newBuilder()
            .setAuthorId(4)
            .setFirstName("Virginia")
            .setLastName("Woolf")
            .setGender("Female")
            .build());
      }
    };
  }

  public static List<Book> getBooksFromTempDb() {
    return new ArrayList<>() {
      {
        add(Book.newBuilder()
            .setBookId(1)
            .setAuthorId(1)
            .setTitle("Oliver Twist")
            .setPrice(11.1f)
            .setPages(111)
            .build());
        add(Book.newBuilder()
            .setBookId(2)
            .setAuthorId(1)
            .setTitle("A Christmas Carol")
            .setPrice(12.2f)
            .setPages(122)
            .build());
        add(Book.newBuilder()
            .setBookId(3)
            .setAuthorId(2)
            .setTitle("Hamlet")
            .setPrice(23.3f)
            .setPages(233)
            .build());
        add(Book.newBuilder()
            .setBookId(4)
            .setAuthorId(3)
            .setTitle("Harry Potter 1")
            .setPrice(34.4f)
            .setPages(344)
            .build());
        add(Book.newBuilder()
            .setBookId(5)
            .setAuthorId(3)
            .setTitle("The Casual Vacancy")
            .setPrice(35.5f)
            .setPages(355)
            .build());
        add(Book.newBuilder()
            .setBookId(6)
            .setAuthorId(4)
            .setTitle("Mrs Dalloway>")
            .setPrice(46.6f)
            .setPages(466)
            .build());
      }
    };
  }
}
