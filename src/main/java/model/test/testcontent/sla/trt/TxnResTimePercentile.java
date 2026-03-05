package model.test.testcontent.sla.trt;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxnResTimePercentile {

    @JsonProperty("Percentile")
    private int percentile;

    @JsonProperty("Transactions")
    List<Transaction> transactions;

}
