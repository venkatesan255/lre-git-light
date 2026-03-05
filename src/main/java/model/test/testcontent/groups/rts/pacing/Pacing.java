package model.test.testcontent.groups.rts.pacing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.PacingStartNewIterationType;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pacing {

    @JsonProperty("NumberOfIterations")
    @Builder.Default
    private int numberOfIterations = 1;

    @JsonProperty("StartNewIteration")
    @Builder.Default
    private StartNewIteration startNewIteration = new StartNewIteration();


    // Inner class for StartNewIteration
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StartNewIteration {

        @JsonProperty("Type")
        @Builder.Default
        private PacingStartNewIterationType type = PacingStartNewIterationType.IMMEDIATELY;

        @JsonProperty("DelayOfSeconds")
        private Integer delayOfSeconds;

        @JsonProperty("DelayAtRangeOfSeconds")
        private Integer delayAtRangeOfSeconds;

        @JsonProperty("DelayAtRangeToSeconds")
        private Integer delayAtRangeToSeconds;

    }
}
