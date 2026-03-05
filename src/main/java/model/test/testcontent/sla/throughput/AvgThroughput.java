package model.test.testcontent.sla.throughput;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvgThroughput {

    @JsonProperty("Threshold")
     private Float threshold;
}
