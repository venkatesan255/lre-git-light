package model.test.testcontent.groups.rts.jmeter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JMeter {

    @JsonProperty("JREPath")
     private String jrePath;

    @JsonProperty("AdditionalParameters")
     private String additionalParameters;

    @JsonProperty("StartMeasurements")
     private Boolean startMeasurements;

    @JsonProperty("JMeterHomePath")
     private String jMeterHomePath;

    @JsonProperty("JMeterUseDefaultPort")
     private Boolean jMeterUseDefaultPort;

    @JsonProperty("JMeterMinPort")
     private Integer jMeterMinPort;

    @JsonProperty("JMeterMaxPort")
     private Integer jMeterMaxPort;
}
