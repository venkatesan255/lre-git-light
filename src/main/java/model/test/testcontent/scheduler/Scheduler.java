package model.test.testcontent.scheduler;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.testcontent.scheduler.action.Action;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
 @JsonInclude(JsonInclude.Include.NON_EMPTY) // skip null or empty lists in XML
public class Scheduler {

    @JsonProperty("Actions")
    @JsonDeserialize(as = ArrayList.class) // always deserialize as ArrayList
    @Builder.Default
    private List<Action> actions = new ArrayList<>();

    public void setActions(List<Action> actions) {
        this.actions = (actions == null) ? new ArrayList<>() : new ArrayList<>(actions);
    }

}
