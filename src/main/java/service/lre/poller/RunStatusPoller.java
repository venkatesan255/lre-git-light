package service.lre.poller;

import client.api.lre.RunApiClient;
import client.model.run.RunConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import model.enums.PostRunAction;
import model.enums.RunState;
import model.run.RunStatusExtended;
import service.lre.monitor.RunStatusMonitor;
import util.datetime.DateTimeUtils;
import util.string.StringFormatUtils;

import java.util.concurrent.TimeUnit;


@Slf4j
public class RunStatusPoller {

    private static final long DEFAULT_POLL_INTERVAL_SECONDS = 15;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_SECONDS = 5;

    private final RunStatusMonitor statusMonitor;
    private final RunProgressCalculator progressCalculator;
    private final RunStatusFormatter statusFormatter;
    private final PostRunAction postRunAction;
    private final long pollIntervalSeconds;
    private final long timeslotDurationMillis;
    private final int runId;

    @Getter
    private boolean aborted = false;

    @Getter
    private String abortReason = null;

    // Dynamic logging intervals
    private static final long SHORT_RUN_THRESHOLD = TimeUnit.MINUTES.toMillis(30);
    private static final long MEDIUM_RUN_THRESHOLD = TimeUnit.HOURS.toMillis(2);
    private static final long LOG_INTERVAL_SHORT = TimeUnit.MINUTES.toMillis(1);
    private static final long LOG_INTERVAL_MEDIUM = TimeUnit.MINUTES.toMillis(3);
    private static final long LOG_INTERVAL_LONG = TimeUnit.MINUTES.toMillis(5);

    public RunStatusPoller(RunApiClient apiClient, RunConfig runConfig, int runId) {
        this.runId = runId;
        this.timeslotDurationMillis = runConfig.timeslot().toMillis();
        this.statusMonitor = new RunStatusMonitor(apiClient, runConfig, runId);
        this.progressCalculator = new RunProgressCalculator(timeslotDurationMillis);
        this.statusFormatter = new RunStatusFormatter(runId, timeslotDurationMillis);
        this.postRunAction = runConfig.postRunAction();
        this.pollIntervalSeconds = DEFAULT_POLL_INTERVAL_SECONDS;
    }

    public RunStatusExtended pollUntilDone() {
        long startTime = System.currentTimeMillis();
        int consecutiveFailures = 0;
        RunStatusExtended lastKnownStatus = createInitialStatus();

        long lastLogTime = startTime;
        RunState lastState = RunState.UNDEFINED;

        while (!Thread.currentThread().isInterrupted()) {

            if (isTimeslotExceeded(startTime)) {
                aborted = true;
                abortReason = "Timeslot exceeded";
                logTimeslotExceeded();
                return lastKnownStatus;
            }

            try {
                PollResult result = pollOnce(startTime, lastState, lastLogTime);

                if (result.shouldReturn()) {
                    return result.status();
                }

                lastKnownStatus = result.status();
                lastState = RunState.fromValue(result.status().getRunState());
                lastLogTime = result.lastLogTime();
                consecutiveFailures = 0;

                sleep(pollIntervalSeconds);

            } catch (Exception e) {
                consecutiveFailures = handleFailure(consecutiveFailures, e);

                if (consecutiveFailures >= MAX_RETRIES) {
                    log.error("Max retries exceeded for run [{}]. Returning last known status.", runId);
                    return lastKnownStatus;
                }
            }
        }

        log.debug("Polling interrupted for run [{}]. Returning last known status: {}",
                runId, lastKnownStatus.getRunState());

        return lastKnownStatus;
    }

    private PollResult pollOnce(long startTime, RunState lastState, long lastLogTime) {
        RunStatusExtended currentStatus = statusMonitor.fetchCurrentStatus();
        RunState currentState = RunState.fromValue(currentStatus.getRunState());

        long updatedLogTime = logStatus(currentState, startTime, lastState, lastLogTime);

        if (isTerminalState(currentState)) {
            return PollResult.returnStatus(currentStatus, updatedLogTime);
        }

        // Check for abort conditions (sets flags but doesn't stop polling)
        checkAbortConditions(currentStatus, currentState);

        return PollResult.continuePolling(currentStatus, updatedLogTime);
    }

    private void checkAbortConditions(RunStatusExtended currentStatus, RunState currentState) {
        if (!aborted && statusMonitor.shouldAbortDueToErrors(currentStatus, currentState)) {
            statusMonitor.abortRunDueToErrors(currentStatus);
            aborted = true;
            abortReason = "Threshold breach detected";
        }
    }
    private RunStatusExtended createInitialStatus() {
        RunStatusExtended status = new RunStatusExtended();
        status.setRunState(RunState.UNDEFINED.getValue());
        return status;
    }

    private boolean isTerminalState(RunState currentState) {
        if (!postRunAction.isTerminal(currentState)) {
            return false;
        }

        log.info("Run [{}] reached terminal state [{}] for PostRunAction [{}]",
                runId, currentState, postRunAction);

        return true;
    }

    private boolean isTimeslotExceeded(long startTime) {
        return System.currentTimeMillis() - startTime > timeslotDurationMillis;
    }

    private void logTimeslotExceeded() {
        log.info("Run [{}] reached timeslot limit ({}). Stopping monitoring.",
                runId, DateTimeUtils.formatDuration(timeslotDurationMillis));
    }

    private long logStatus(RunState state, long startTime, RunState lastState, long lastLogTime) {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - startTime;

        if (shouldLog(state, now, lastState, lastLogTime)) {
            long remainingMillis = Math.max(0, timeslotDurationMillis - elapsedMillis);
            int progress = progressCalculator.calculateProgress(state, elapsedMillis);

            String progressBar = StringFormatUtils.buildProgressBar(progress);
            String logMessage = statusFormatter.formatStatusLog(
                    state, elapsedMillis, remainingMillis, progress, progressBar);

            log.info("{}", logMessage);

            return now;
        }

        return lastLogTime;
    }

    private boolean shouldLog(RunState state, long currentTime, RunState lastState, long lastLogTime) {
        return state != lastState || currentTime - lastLogTime >= getDynamicLogInterval();
    }

    private long getDynamicLogInterval() {
        if (timeslotDurationMillis <= SHORT_RUN_THRESHOLD) return LOG_INTERVAL_SHORT;
        else if (timeslotDurationMillis <= MEDIUM_RUN_THRESHOLD) return LOG_INTERVAL_MEDIUM;
        else return LOG_INTERVAL_LONG;
    }

    private int handleFailure(int consecutiveFailures, Exception e) {
        consecutiveFailures++;
        String msg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();

        log.warn("Failed to fetch status for run [{}]: {} (Attempt {}/{})",
                runId, msg, consecutiveFailures, MAX_RETRIES);

        sleep(RETRY_DELAY_SECONDS);
        return consecutiveFailures;
    }

    private void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Polling interrupted for run [{}]", runId);
        }
    }

    private record PollResult(RunStatusExtended status, boolean shouldReturn, long lastLogTime) {

        static PollResult returnStatus(RunStatusExtended status, long lastLogTime) {
            return new PollResult(status, true, lastLogTime);
        }

        static PollResult continuePolling(RunStatusExtended status, long lastLogTime) {
            return new PollResult(status, false, lastLogTime);
        }
    }
}