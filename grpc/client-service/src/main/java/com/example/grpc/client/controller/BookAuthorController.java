package com.example.grpc.client.controller;

import com.example.grpc.client.service.BookAuthorClientService;
import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class BookAuthorController {

  BookAuthorClientService bookAuthorClientService;

  @GetMapping("/author/{authorId}")
  public Map<Descriptors.FieldDescriptor, Object> getAuthor(
      @PathVariable String authorId) {
    return bookAuthorClientService.getAuthor(Integer.parseInt(authorId));
  }
}
