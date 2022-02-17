package dev.darvega.streamsschedule.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MutableLiveStream {

  private String id;
  private String title;
  private String description;
  private String url;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

}
