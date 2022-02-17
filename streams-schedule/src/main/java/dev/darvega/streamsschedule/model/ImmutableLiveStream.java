package dev.darvega.streamsschedule.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ImmutableLiveStream {

  private final String id;
  private final String title;
  private final String description;
  private final String url;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;

}
