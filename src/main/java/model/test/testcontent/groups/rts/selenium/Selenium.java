package model.test.testcontent.groups.rts.selenium;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Selenium {

    @JsonProperty("JREPath")
     private String jrePath;

    @JsonProperty("ClassPath")
     private String classPath;

    @JsonProperty("TestNgFiles")
     private String testNgFiles;
}
