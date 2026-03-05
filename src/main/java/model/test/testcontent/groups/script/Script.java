package model.test.testcontent.groups.script;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Script {

    @JsonProperty("ID")
     private int id;

    @JsonProperty("Name")
     private String name;

    @JsonProperty("CreatedBy")
     private String createdBy;

    @JsonProperty("TestFolderPath")
     private String testFolderPath;

    @JsonProperty("WorkingMode")
     private String workingMode;

    @JsonProperty("Protocol")
     private String protocol;

    @JsonProperty("LastModifyDate")
     private String lastModifyDate;

    @JsonProperty("CreationDate")
     private String creationDate;

    @JsonProperty("IsScriptLocked")
     private Boolean isScriptLocked;

    @JsonProperty("SplitScriptResponse")
     private String splitScriptResponse;


    @JsonProperty("ProtocolType")
     private String protocolType;


}
