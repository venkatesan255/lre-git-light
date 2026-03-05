package model.test.testcontent.groups.rts.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.LogOptionsType;
import model.enums.LogType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @JsonProperty("Type")
     private LogType type = LogType.STANDARD; // ignore, standard, extended

    @JsonProperty("LogOptions")
     private LogOptions logOptions;

    @JsonProperty("ParametersSubstitution")
     private Boolean parametersSubstitution;

    @JsonProperty("DataReturnedByServer")
     private Boolean dataReturnedByServer;

    @JsonProperty("AdvanceTrace")
     private Boolean advanceTrace;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogOptions {

        @JsonProperty("Type")
         private LogOptionsType type = LogOptionsType.ON_ERROR; // on error / always

        @JsonProperty("CacheSize")
         private Integer cacheSize = 1;
    }
}
