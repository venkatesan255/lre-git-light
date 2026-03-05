package model.test.testcontent.scheduler.action.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.SchedulerVusersType;
import model.test.testcontent.scheduler.action.Action;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class VusersAction {

    @JsonProperty("Type")
    private SchedulerVusersType type = SchedulerVusersType.SIMULTANEOUSLY;

    @JsonProperty("Vusers")
    private Integer vusers;

    @JsonProperty("Ramp")
    private Ramp ramp;

    public void setVusersFromString(String vusers) {
        setVusers(Integer.parseInt(vusers));
    }

    public abstract void applyTo(Action.ActionBuilder builder);
}
