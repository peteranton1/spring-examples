package dev.darvega.streamsschedule.controller;

import dev.darvega.streamsschedule.model.LiveStream;
import dev.darvega.streamsschedule.repository.LiveStreamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/streams")
public class LiveStreamController {

  private final LiveStreamRepository repository;

  public LiveStreamController(LiveStreamRepository repository) {
    this.repository = repository;
  }

  // http://localhost/streams
  @GetMapping("")
  public List<LiveStream> findAll() {
    return repository.findAll();
  }

  // http://localhost/streams/1234-2345-1234-23
  @GetMapping("/{id}")
  public LiveStream findById(
      @PathVariable String id) {
    return repository.findById(id);
  }

  // POST http://localhost/streams
  @PostMapping(
      consumes = {MediaType.APPLICATION_JSON_VALUE}
      , produces = {MediaType.APPLICATION_JSON_VALUE}
  )
  @ResponseStatus(HttpStatus.CREATED)
  public LiveStream create(
      @RequestBody LiveStream stream) {
    return repository.create(stream);
  }

  // PUT http://localhost/streams
  @PutMapping(
      consumes = {MediaType.APPLICATION_JSON_VALUE}
      , produces = {MediaType.APPLICATION_JSON_VALUE}
  )
  @ResponseStatus(HttpStatus.OK)
  public LiveStream update(
      @RequestBody LiveStream stream
  ) {
    repository.update(stream, stream.id());
    return repository.findById(stream.id());
  }

  // DELETE http://localhost/streams/1111
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public LiveStream delete(
      @PathVariable String id
  ) {
    LiveStream existing = repository.findById(id);
    repository.delete(id);
    return existing;
  }

}
