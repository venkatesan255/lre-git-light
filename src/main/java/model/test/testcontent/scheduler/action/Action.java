package model.test.testcontent.scheduler.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.testcontent.scheduler.action.duration.Duration;
import model.test.testcontent.scheduler.action.initialize.Initialize;
import model.test.testcontent.scheduler.action.startgroup.StartGroup;
import model.test.testcontent.scheduler.action.startvusers.StartVusers;
import model.test.testcontent.scheduler.action.stopvusers.StopVusers;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
 public class Action {

    @JsonProperty("Initialize")
     private Initialize initialize;

    @JsonProperty("StartVusers")
     private StartVusers startVusers;

    @JsonProperty("StopVusers")
     private StopVusers stopVusers;

    @JsonProperty("Duration")
     private Duration duration;

    @JsonProperty("StartGroup")
     private StartGroup startGroup;


}
