package com.example.grpc.server;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import com.example.db.TempDb;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

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

}
