package model.test.testcontent.groups.rts.javavm;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
 public class JavaVM {

    @JsonProperty("JavaEnvClassPaths")
     private List<String> javaEnvClassPaths;

    @JsonProperty("UserSpecifiedJdk")
     private Boolean userSpecifiedJdk;

    @JsonProperty("JdkHome")
     private String jdkHome;

    @JsonProperty("JavaVmParameters")
     private String javaVmParameters;

    @JsonProperty("UseXboot")
     private Boolean useXboot;

    @JsonProperty("EnableClassLoaderPerVuser")
     private Boolean enableClassLoaderPerVuser;
}
