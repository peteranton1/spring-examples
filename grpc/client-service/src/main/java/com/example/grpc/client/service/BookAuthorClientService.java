package com.example.grpc.client.service;

import com.example.Author;
import com.example.Book;
import com.example.BookAuthorServiceGrpc;
import com.example.db.TempDb;
import com.google.protobuf.Descriptors;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class BookAuthorClientService {

  @GrpcClient("grpc-example-service")
  BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;

  @GrpcClient("grpc-example-service")
  BookAuthorServiceGrpc.BookAuthorServiceStub asynchronousClient;

  public Map<Descriptors.FieldDescriptor, Object> getAuthor(int authorId) {
    Author authorRequest = Author.newBuilder()
        .setAuthorId(authorId).build();
    Author authorResponse = synchronousClient.getAuthor(authorRequest);
    return authorResponse.getAllFields();
  }

  public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthor(int authorId) throws InterruptedException {
    final CountDownLatch countDownLatch = new CountDownLatch(1);

    Author authorRequest = Author.newBuilder()
        .setAuthorId(authorId).build();

    final List<Map<Descriptors.FieldDescriptor, Object>> response =
        new ArrayList<>();

    asynchronousClient.getBooksByAuthor(authorRequest,
        new StreamObserver<>() {
          @Override
          public void onNext(Book book) {
            response.add(book.getAllFields());
          }

          @Override
          public void onError(Throwable throwable) {
            countDownLatch.countDown();
          }

          @Override
          public void onCompleted() {
            countDownLatch.countDown();
          }
        });

    boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
    return await ? response : Collections.emptyList();
  }

  public Map<String, Map<Descriptors.FieldDescriptor, Object>> getExpensiveBook() throws InterruptedException {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new HashMap<>();
    StreamObserver<Book> responseObserver = asynchronousClient.getExpensiveBook(
        new StreamObserver<>() {

          @Override
          public void onNext(Book book) {
            response.put("ExpensiveBook", book.getAllFields());
          }

          @Override
          public void onError(Throwable throwable) {
            countDownLatch.countDown();
          }

          @Override
          public void onCompleted() {
            countDownLatch.countDown();
          }
        }
    );

    TempDb.getBooksFromTempDb().forEach(responseObserver::onNext);
    responseObserver.onCompleted();
    boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
    return await ? response : Collections.emptyMap();
  }

  public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthorGender(String gender) throws InterruptedException {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
    StreamObserver<Book> responseObserver = asynchronousClient.getBookByAuthorGender(
        new StreamObserver<>() {
          @Override
          public void onNext(Book book) {
            response.add(book.getAllFields());
          }

          @Override
          public void onError(Throwable throwable) {
            countDownLatch.countDown();
          }

          @Override
          public void onCompleted() {
            countDownLatch.countDown();
          }
        }
    );

    TempDb.getAuthorsFromTempDb()
        .stream()
        .filter(author -> author.getGender().equalsIgnoreCase(gender))
        .forEach(author -> responseObserver
            .onNext(Book.newBuilder()
                .setAuthorId(author.getAuthorId()).build()));

    responseObserver.onCompleted();

    boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
    return await ? response : Collections.emptyList();
  }
}
