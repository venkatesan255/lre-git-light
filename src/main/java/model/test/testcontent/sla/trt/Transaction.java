package model.test.testcontent.sla.trt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.testcontent.sla.common.Thresholds;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Threshold")
    private Float threshold;

    @JsonProperty("Thresholds")
    private Thresholds thresholds;


}
