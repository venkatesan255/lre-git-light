package model.test.testcontent.scheduler.action.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ramp {
    @JsonProperty("Vusers")
     private Integer vusers;

    @JsonProperty("TimeInterval")
     private TimeInterval timeInterval;


}
