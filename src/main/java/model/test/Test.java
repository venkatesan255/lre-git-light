package model.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.testcontent.TestContent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("LastModified")
    private String lastModified;

    @JsonProperty("TestFolderPath")
    private String testFolderPath;

    @JsonProperty("Content")
    private TestContent content;

}

