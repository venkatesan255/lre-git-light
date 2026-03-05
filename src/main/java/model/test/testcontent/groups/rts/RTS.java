package model.test.testcontent.groups.rts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.test.testcontent.groups.rts.javavm.JavaVM;
import model.test.testcontent.groups.rts.jmeter.JMeter;
import model.test.testcontent.groups.rts.log.Log;
import model.test.testcontent.groups.rts.pacing.Pacing;
import model.test.testcontent.groups.rts.selenium.Selenium;
import model.test.testcontent.groups.rts.thinktime.ThinkTime;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RTS {

    @JsonProperty("Name")
     private String name;

    @JsonProperty("Pacing")
     private Pacing pacing;

    @JsonProperty("ThinkTime")
     private ThinkTime thinkTime;

    @JsonProperty("Log")
     private Log lreLog;

    @JsonProperty("JavaVM")
     private JavaVM javaVM;

    @JsonProperty("JMeter")
     private JMeter jmeterSettings;

    @JsonProperty("Selenium")
     private Selenium seleniumSettings;

}