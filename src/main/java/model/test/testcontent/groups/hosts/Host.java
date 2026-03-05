package model.test.testcontent.groups.hosts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.HostType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Host {

    @JsonProperty("Name")
     private String name;

    @JsonProperty("Type")
     private HostType type;

    @JsonProperty("HostTemplateId")
     private String hostTemplateId;


}
