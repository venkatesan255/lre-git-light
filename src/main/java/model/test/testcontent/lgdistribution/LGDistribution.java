package model.test.testcontent.lgdistribution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.LGDistributionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LGDistribution {

    @JsonProperty("Type")
     private LGDistributionType type;

    @JsonProperty("Amount")
     private Integer amount;


    public LGDistribution(LGDistributionType type) {
        this(type, null);
    }

}
