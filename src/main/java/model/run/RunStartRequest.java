package model.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.PostRunAction;
import model.timeslot.Timeslot;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RunStartRequest {

    @JsonProperty("PostRunAction")
    private PostRunAction postRunAction;

    @JsonProperty("TestID")
    private int testId;

    @JsonProperty("TestInstanceID")
    private Integer testInstanceId;

    @JsonProperty("TimeslotDuration")
    private Timeslot timeslot;

    @JsonProperty("vudsMode")
    @Builder.Default
    private boolean vudsMode = false;
}
