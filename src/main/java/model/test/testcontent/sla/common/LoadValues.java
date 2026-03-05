package model.test.testcontent.sla.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadValues {

    @JsonProperty("Betweens")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private List<Between> between = new ArrayList<>();

    @JsonProperty("LessThan")
    private Float lessThan;

    @JsonProperty("GreaterThanOrEqual")
    private Float greaterThanOrEqual;

}
