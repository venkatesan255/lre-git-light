package service;

import client.api.lre.RunApiClient;
import client.model.run.RunConfig;
import client.model.run.TestExecutionResult;
import model.run.RunStartRequest;
import exception.http.BadRequestException;
import exception.http.NotFoundException;
import exception.http.ServerErrorException;
import exception.http.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import client.model.test.LreTest;
import model.enums.RunState;
import model.run.RunStartResponse;
import model.run.RunStatusExtended;
import service.lre.poller.RunStatusPoller;

@Slf4j
@RequiredArgsConstructor
public class RunService {

    private static final int RUN_FAILED = -1;

    private final RunApiClient runApiClient;

    public TestExecutionResult executeRun(RunConfig runConfig, LreTest resolvedTest) {
        try {
            log.info("Starting test: {} (ID: {})", resolvedTest.testName(), resolvedTest.testId());
            int runId = createAndStartRun(runConfig, resolvedTest);
            return monitorRunCompletion(runConfig, runId);

        } catch (ServiceUnavailableException e) {
            log.error("Service unavailable for test {}: {}", resolvedTest.testName(), e.getMessage());
            return failedResult("Service unavailable: " + e.getMessage());

        } catch (BadRequestException | NotFoundException | ServerErrorException e) {
            log.error("Failed to execute run for test {}: {}", resolvedTest.testName(), e.getMessage());
            return failedResult(e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error executing run for test: {}", resolvedTest.testName(), e);
            return failedResult("Unexpected error: " + e.getMessage());
        }
    }

    private int createAndStartRun(RunConfig runConfig, LreTest resolvedTest) {
        RunStartRequest request = buildRunStartRequest(runConfig, resolvedTest);
        RunStartResponse response = runApiClient.createRun(request);
        int runId = response.getId();
        log.info("Run created successfully with ID: {}", runId);
        return runId;
    }

    private RunStartRequest buildRunStartRequest(RunConfig runConfig, LreTest resolvedTest) {
        return RunStartRequest.builder()
                .postRunAction(runConfig.postRunAction())
                .testId(resolvedTest.testId())
                .timeslot(runConfig.timeslot())
                .build();
    }

    private TestExecutionResult monitorRunCompletion(RunConfig runConfig, int runId) {
        log.debug("Starting status polling for run ID: {}", runId);
        RunStatusPoller poller = new RunStatusPoller(runApiClient, runConfig, runId);
        RunStatusExtended finalStatus = poller.pollUntilDone();

        if (poller.isAborted()) {
            String reason = poller.getAbortReason() != null
                    ? poller.getAbortReason()
                    : "unknown reason";
            log.error("Run [{}] aborted - {}", runId, reason);
            return TestExecutionResult.builder()
                    .runId(runId)
                    .testFailed(true)
                    .failureReason(reason)
                    .build();
        }

        return evaluateRunResult(runId, finalStatus.getRunState());
    }

    private TestExecutionResult evaluateRunResult(int runId, String runState) {
        log.info("Run [{}] completed with state: {}", runId, runState);
        boolean failed = isRunFailure(runState);

        if (failed) {
            log.warn("Run [{}] finished with non-successful state: {}", runId, runState);
        } else {
            log.info("Run [{}] finished successfully", runId);
        }

        return TestExecutionResult.builder()
                .runId(runId)
                .testFailed(failed)
                .failureReason(failed ? "Run completed with state: " + runState : null)
                .build();
    }

    private TestExecutionResult failedResult(String reason) {
        return TestExecutionResult.builder()
                .runId(RUN_FAILED)
                .testFailed(true)
                .failureReason(reason)
                .build();
    }

    private boolean isRunFailure(String runState) {
        RunState state = RunState.fromValue(runState);
        return state == RunState.RUN_FAILURE || state == RunState.CANCELED;
    }
}