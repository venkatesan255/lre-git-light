package model.test.testcontent.groups;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.testcontent.groups.hosts.Host;
import model.test.testcontent.groups.rts.RTS;
import model.test.testcontent.groups.script.Script;
import model.test.testcontent.scheduler.Scheduler;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Vusers")
    private Integer vusers;

    @JsonProperty("Script")
    private Script script;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("CommandLine")
    private String commandLine;

    @JsonProperty("GlobalRTS")
    private String globalRTS;

    @JsonProperty("GlobalCommandLine")
    private String globalCommandLine;

    @JsonProperty("Hosts")
    private List<Host> hosts;

    @JsonProperty("RTS")
    private RTS rts;

    @JsonProperty("Scheduler")
    private Scheduler scheduler;

}
