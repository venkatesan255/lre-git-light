package model.test.testcontent.sla.hits;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalHits {
    @JsonProperty("Threshold")
     private Float threshold;
}
