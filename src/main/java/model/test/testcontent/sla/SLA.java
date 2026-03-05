package model.test.testcontent.sla;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.testcontent.sla.errors.ErrorsPerSecond;
import model.test.testcontent.sla.hits.AvgHitsPerSecond;
import model.test.testcontent.sla.hits.TotalHits;
import model.test.testcontent.sla.throughput.AvgThroughput;
import model.test.testcontent.sla.throughput.TotalThroughput;
import model.test.testcontent.sla.trt.TxnResTimeAverage;
import model.test.testcontent.sla.trt.TxnResTimePercentile;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SLA {

    @JsonProperty("TransactionResponseTimePercentile")
    private TxnResTimePercentile txnResTimePercentile;

    @JsonProperty("TransactionResponseTimeAverage")
    private TxnResTimeAverage txnResTimeAverage;

    @JsonProperty("ErrorsPerSecond")
    private ErrorsPerSecond errorsPerSecond;

    @JsonProperty("TotalHits")
    private TotalHits totalHits;

    @JsonProperty("AverageHitsPerSecond")
    private AvgHitsPerSecond avgHitsPerSecond;

    @JsonProperty("TotalThroughput")
    private TotalThroughput totalThroughput;

    @JsonProperty("AverageThroughput")
    private AvgThroughput avgThroughput;


}
