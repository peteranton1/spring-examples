package com.example.grpc.server;

import com.example.Author;
import com.example.Book;
import com.example.BookAuthorServiceGrpc;
import com.example.db.TempDb;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class BookAuthorServerService extends
    BookAuthorServiceGrpc.BookAuthorServiceImplBase {

  @Override
  public void getAuthor(Author request,
                        StreamObserver<Author> responseObserver) {
    try {
      Author authorResponse = TempDb.getAuthorsFromTempDb().stream()
          .filter(author ->
              author.getAuthorId()
                  == request.getAuthorId())
          .findFirst()
          .orElseThrow(() -> new ServerException(
              "Not Found - Author Id: " +
                  request.getAuthorId()));
      responseObserver.onNext(authorResponse);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
      throw e;
    }
  }

  @Override
  public void getBooksByAuthor(Author request, StreamObserver<Book> responseObserver) {
    TempDb.getBooksFromTempDb().stream()
        .filter(book -> book.getAuthorId() == request.getAuthorId())
        .forEach(responseObserver::onNext);
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<Book> getExpensiveBook(StreamObserver<Book> responseObserver) {
    return new StreamObserver<>() {
      Book expensiveBook = null;
      float priceTrack = 0;
      @Override
      public void onNext(Book book) {
        if(book.getPrice() > priceTrack){
          priceTrack = book.getPrice();
          expensiveBook = book;
        }
      }

      @Override
      public void onError(Throwable throwable) {
        responseObserver.onError(throwable);
      }

      @Override
      public void onCompleted() {
        responseObserver.onNext(expensiveBook);
        responseObserver.onCompleted();
      }
    };
  }

  @Override
  public StreamObserver<Book> getBookByAuthorGender(StreamObserver<Book> responseObserver) {
    return new StreamObserver<>() {
      final List<Book> bookList = new ArrayList<>();
      @Override
      public void onNext(Book book) {
        TempDb.getBooksFromTempDb()
            .stream()
            .filter(booksFromDb -> book.getAuthorId() == booksFromDb.getAuthorId())
            .forEach(bookList::add);
      }

      @Override
      public void onError(Throwable throwable) {
        responseObserver.onError(throwable);
      }

      @Override
      public void onCompleted() {
        bookList.forEach(responseObserver::onNext);
        responseObserver.onCompleted();
      }
    };
  }
}
