package com.example.restreactive.mapping;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class ZonedDateTimeHelper {
    public static final ZoneId ZONE_ID = ZoneId.of("UTC");

    public ZonedDateTime ensureZone(ZonedDateTime theTime) {
        if (Objects.isNull(theTime)) {
            return theTime;
        }
        return theTime.withZoneSameInstant(ZONE_ID);
    }
}