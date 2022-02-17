package dev.darvega.streamsschedule.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LiveStreamTest {

  @Test
  void create_new_mutable_live_stream() {
    MutableLiveStream stream = MutableLiveStream.builder()
        .id(UUID.randomUUID().toString())
        .title("Building REST APIs with Spring Boot")
        .description("""
                Spring Boot is a very convenient way
                to build a web application.
                """)
        .url("https://www.twtich.tv/danvega")
        .startDate(LocalDateTime.of(2022,2,22,16,11,32))
        .endDate(LocalDateTime.of(2022,2,22,17,11,32))
        .build();

    assertNotNull(stream);
    assertEquals("Building REST APIs with Spring Boot", stream.getTitle());
  }

  @Test
  void create_new_immutable_live_stream() {
    ImmutableLiveStream stream = ImmutableLiveStream.builder()
        .id(UUID.randomUUID().toString())
        .title("Building REST APIs with Spring Boot")
        .description("""
                Spring Boot is a very convenient way
                to build a web application.
                """)
        .url("https://www.twtich.tv/danvega")
        .startDate(LocalDateTime.of(2022,2,22,16,11,32))
        .endDate(LocalDateTime.of(2022,2,22,17,11,32))
        .build();

    assertNotNull(stream);
    assertEquals("Building REST APIs with Spring Boot", stream.getTitle());
  }

  @Test
  void create_new_record_live_stream() {
    LiveStream stream = new LiveStream(
        UUID.randomUUID().toString(),
        "Building REST APIs with Spring Boot",
        """
                Spring Boot is a very convenient way
                to build a web application.
                """,
        "https://www.twtich.tv/danvega",
        LocalDateTime.of(2022,2,22,16,11,32),
        LocalDateTime.of(2022,2,22,17,11,32)
    );

    assertNotNull(stream);
    assertEquals("Building REST APIs with Spring Boot", stream.title());
  }

}