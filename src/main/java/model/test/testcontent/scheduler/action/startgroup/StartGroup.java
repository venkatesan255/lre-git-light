package model.test.testcontent.scheduler.action.startgroup;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.SchedulerStartGroupType;
import model.test.testcontent.scheduler.action.common.TimeInterval;

@Data
@AllArgsConstructor
@NoArgsConstructor
 public class StartGroup {

    @JsonProperty("Type")
     private SchedulerStartGroupType type = SchedulerStartGroupType.IMMEDIATELY;

    @JsonProperty("TimeInterval")
     private TimeInterval timeInterval;

    @JsonProperty("Name")
     private String name;
}