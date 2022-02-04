package com.example.grpc.client.service;

import com.example.Author;
import com.example.Book;
import com.example.BookAuthorServiceGrpc;
import com.google.protobuf.Descriptors;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookAuthorClientService {

  @GrpcClient("grpc-example-service")
  BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;

  public Map<Descriptors.FieldDescriptor, Object> getAuthor(int authorId) {
    Author authorRequest = Author.newBuilder()
        .setAuthorId(authorId).build();
    Author authorResponse = synchronousClient.getAuthor(authorRequest);
    return authorResponse.getAllFields();
  }

}
