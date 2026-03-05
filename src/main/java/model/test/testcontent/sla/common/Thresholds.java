package model.test.testcontent.sla.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Thresholds {

    @JsonProperty("LessThanThreshold")
    private Float lessThanThreshold;

    @JsonProperty("BetweenThreshold")
    private BetweenThreshold betweenThreshold = new BetweenThreshold();

    @JsonProperty("GreaterThanOrEqualThreshold")
    private Float greaterThanOrEqualThreshold;
}
