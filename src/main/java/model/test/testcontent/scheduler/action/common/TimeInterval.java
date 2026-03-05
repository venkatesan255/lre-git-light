package model.test.testcontent.scheduler.action.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeInterval {

    @JsonProperty("Days")
     private Integer days;

    @JsonProperty("Hours")
     private Integer hours;

    @JsonProperty("Minutes")
     private Integer minutes;

    @JsonProperty("Seconds")
     private Integer seconds;

}
