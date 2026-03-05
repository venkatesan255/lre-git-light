package model.test.testcontent;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.test.testcontent.analysistemplate.AnalysisTemplate;
import model.test.testcontent.automatictrending.AutomaticTrending;
import model.test.testcontent.groups.Group;
import model.test.testcontent.groups.commandline.CommandLine;
import model.test.testcontent.groups.rts.RTS;
import model.test.testcontent.hostattributes.HostAttribute;
import model.test.testcontent.lgdistribution.LGDistribution;
import model.test.testcontent.monitorofw.MonitorOFW;
import model.test.testcontent.monitorprofile.MonitorProfile;
import model.test.testcontent.scheduler.Scheduler;
import model.test.testcontent.sla.SLA;
import model.test.testcontent.workloadtype.WorkloadType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestContent {

    @JsonProperty("Controller")
    private String controller;

    @JsonProperty("WorkloadType")
    private WorkloadType workloadType;

    @JsonProperty("LGDistribution")
    private LGDistribution lgDistribution;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("MonitorProfiles")
    private List<MonitorProfile> monitorProfiles = new ArrayList<>();

    @JsonProperty("TotalVusers")
    private Integer totalVusers;

    @JsonProperty("MonitorsOFW")
    private List<MonitorOFW> monitorOFWIds = new ArrayList<>();

    @JsonProperty("GlobalRTSs")
    private List<RTS> globalRts;

    @JsonProperty("GlobalCommandLines")
    private List<CommandLine> globalCommandLines;

    @JsonProperty("Groups")
    private List<Group> groups = new ArrayList<>();

    @JsonProperty("Scheduler")
    private Scheduler scheduler;

    @JsonProperty("SLA")
    private SLA sla;

    @JsonProperty("AnalysisTemplate")
    private AnalysisTemplate analysisTemplate;

    @JsonProperty("AutomaticTrending")
    private AutomaticTrending automaticTrending;

    @JsonProperty("HostAttributes")
    private List<HostAttribute> hostAttributes;
}