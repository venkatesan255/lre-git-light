package model.test.testcontent.scheduler.action.duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.SchedulerDurationType;
import model.test.testcontent.scheduler.action.common.TimeInterval;

@Data
@NoArgsConstructor
@AllArgsConstructor
 public class Duration {

    @JsonProperty("Type")
     private SchedulerDurationType type = SchedulerDurationType.UNTIL_COMPLETION;

    @JsonProperty("TimeInterval")
     private TimeInterval timeInterval;

}