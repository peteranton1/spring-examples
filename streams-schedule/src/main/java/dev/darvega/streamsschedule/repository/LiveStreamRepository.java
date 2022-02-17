package dev.darvega.streamsschedule.repository;

import dev.darvega.streamsschedule.model.LiveStream;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class LiveStreamRepository {

  private final List<LiveStream> streams = new ArrayList<>();

  public LiveStreamRepository() {
    streams.add(
        new LiveStream(
            //UUID.randomUUID().toString(),
            "1234-2345-1234-23",
            "Building REST APIs with Spring Boot",
            """
                    Spring Boot is a very convenient way
                    to build a web application.
                    """,
            "https://www.twtich.tv/danvega",
            LocalDateTime.of(2022,2,22,16,11,32),
            LocalDateTime.of(2022,2,22,17,11,32)
        )
    );
  }

  public List<LiveStream> findAll() {
    return streams;
  }

  public LiveStream findById(String id) {
    return streams.stream()
        .filter(liveStream -> liveStream.id().equals(id))
        .findFirst().orElse(null);
  }

  public LiveStream create(LiveStream stream) {
    streams.add(stream);
    return stream;
  }

  public void update(LiveStream stream, String id) {
    LiveStream existing = streams.stream()
        .filter(liveStream -> liveStream.id().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Stream not found"));
    int i = streams.indexOf(existing);
    streams.add(i, stream);
  }

  public void delete(String id) {
    streams
        .removeIf(liveStream -> liveStream.id().equals(id));
  }
}
