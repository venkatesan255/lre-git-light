package model.test.testcontent.sla.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetweenThreshold {

    @JsonProperty("Threshold")
    private List<Float> threshold;

}
