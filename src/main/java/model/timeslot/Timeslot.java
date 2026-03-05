package model.timeslot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.concurrent.TimeUnit;

@Builder
public record Timeslot(
        @JsonProperty("Hours")
        int hours,

        @JsonProperty("Minutes")
        int minutes
) {

    public long toMillis() {
        return TimeUnit.HOURS.toMillis(hours) +
                TimeUnit.MINUTES.toMillis(minutes);
    }

    public long toMinutes() {
        return TimeUnit.HOURS.toMinutes(hours) + minutes;
    }

}
