package com.example.restreactive.model;

import com.example.restreactive.model.EntityObject;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "appt_store_slot")
public class StoreSlot implements EntityObject {
    @Id
    @GeneratedValue
    Integer id;
    String slotCode;
    String storeCode;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
