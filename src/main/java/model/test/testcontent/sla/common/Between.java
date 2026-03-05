package model.test.testcontent.sla.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Between {

    @JsonProperty("From")
     private Float from;

    @JsonProperty("To")
     private Float to;

}
