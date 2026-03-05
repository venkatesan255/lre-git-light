package model.test.testcontent.sla.trt;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.SlaLoadCriteria;
import model.test.testcontent.sla.common.LoadValues;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxnResTimeAverage {

    @JsonProperty("LoadCriterion")
    private SlaLoadCriteria loadCriterion;

    @JsonProperty("LoadValues")
    private LoadValues loadValues;

    @JsonProperty("Transactions")
    private List<Transaction> transactions;


}
