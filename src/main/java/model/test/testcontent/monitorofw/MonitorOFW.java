package model.test.testcontent.monitorofw;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorOFW {

    @JsonProperty("ID")
     private Integer id;

}
