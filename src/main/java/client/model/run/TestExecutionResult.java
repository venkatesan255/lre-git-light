package client.model.run;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class TestExecutionResult {

    private int runId;
    private int timeslotId;
    private String dashboardUrl;
    private boolean testFailed;
    private String failureReason;
}
