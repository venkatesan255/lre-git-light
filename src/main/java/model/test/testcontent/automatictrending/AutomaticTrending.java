package model.test.testcontent.automatictrending;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutomaticTrending {

    @JsonProperty("ReportId")
     private Integer reportId;

    @JsonProperty("MaxRunsInReport")
     private Integer maxRuns = 20;

    @JsonProperty("TrendRangeType")
     private TrendRangeType trendRange = TrendRangeType.COMPLETE_RUN;

    @JsonProperty("MaxRunsReachedOption")
     private MaxRunsReachedOption onMaxRuns = MaxRunsReachedOption.DELETE_FIRST_SET_NEW_BASELINE;

    @JsonProperty("StartTime")
     private Integer startTime = 20; // in minutes, required if trendRange = PartOfRun

    @JsonProperty("EndTime")
     private Integer endTime = 80;   // in minutes, required if trendRange = PartOfRun


    public enum TrendRangeType {
        COMPLETE_RUN, PART_OF_RUN
    }

    public enum MaxRunsReachedOption {
        DO_NOT_PUBLISH_ADDITIONAL_RUNS,
        DELETE_FIRST_SET_NEW_BASELINE
    }
}
