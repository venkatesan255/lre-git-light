package model.test.testcontent.scheduler.action.initialize;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.SchedulerInitializeType;
import model.test.testcontent.scheduler.action.common.TimeInterval;

@Data
@AllArgsConstructor
@NoArgsConstructor
 public class Initialize {

    @JsonProperty("Type")
     private SchedulerInitializeType type = SchedulerInitializeType.JUST_BEFORE_VUSER_RUNS;

    @JsonProperty("TimeInterval")
     private TimeInterval timeInterval;

    @JsonProperty("Vusers")
     private Integer vusers;

    @JsonProperty("WaitAfterInit")
     private TimeInterval waitAfterInit;

    public void setVusersFromString(String vusers) {
        this.vusers = Integer.parseInt(vusers);
    }


}