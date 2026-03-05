package model.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunStartResponse {

    @JsonProperty("ID")
    private int id;

    @JsonProperty("Duration")
    private int duration;

    @JsonProperty("RunState")
    private String runState;

    @JsonProperty("RunSLAStatus")
    private String runSlaStatus;

    @JsonProperty("TestID")
    private int testId;

    @JsonProperty("TestInstanceID")
    private int testInstanceId;

    @JsonProperty("PostRunAction")
    private String postRunAction;

    @JsonProperty("TimeslotID")
    private int timeslotId;

    @JsonProperty("VudsMode")
    private boolean vudsMode;
}