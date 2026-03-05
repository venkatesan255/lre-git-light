package model.test.testcontent.groups.rts.thinktime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.ThinkTimeType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThinkTime {

    @JsonProperty("Type")
     private ThinkTimeType type = ThinkTimeType.IGNORE; // ignore, replay, modify, random

    @JsonProperty("MultiplyFactor")
     private Double multiplyFactor;

    @JsonProperty("MinPercentage")
     private Integer minPercentage;

    @JsonProperty("MaxPercentage")
     private Integer maxPercentage;

    @JsonProperty("LimitThinkTimeSeconds")
     private Integer limitThinkTimeSeconds;

}
