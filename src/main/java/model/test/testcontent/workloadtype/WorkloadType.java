package model.test.testcontent.workloadtype;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.WorkloadSubType;
import model.enums.WorkloadTypeEnum;
import model.enums.WorkloadVusersDistributionMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadType {

    @JsonProperty("Type")
    private WorkloadTypeEnum type;

    @JsonProperty("SubType")
    private WorkloadSubType subType;

    @JsonProperty("VusersDistributionMode")
    private WorkloadVusersDistributionMode vusersDistributionMode;

}
