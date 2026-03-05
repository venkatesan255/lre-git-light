package model.test.testcontent.sla.errors;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.SlaLoadCriteria;
import model.test.testcontent.sla.common.LoadValues;
import model.test.testcontent.sla.common.Thresholds;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorsPerSecond {
    @JsonProperty("LoadCriterion")
    private SlaLoadCriteria loadCriterion;

    @JsonProperty("LoadValues")
    private LoadValues loadValues;

    @JsonProperty("Thresholds")
    private Thresholds thresholds;
}
