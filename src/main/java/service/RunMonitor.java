package service;

import client.LreClientException;
import lombok.extern.slf4j.Slf4j;
import model.LreRunConfig;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RunMonitor {

    private final LreRunApiClient client;

    // ---- LRE CI-Optimized Polling ----
    private static final long INITIAL_DELAY_SECONDS = 15;
    private static final long MAX_DELAY_SECONDS = 120;
    private static final int  MAX_RETRIES = 8;
    private static final long RETRY_DELAY_SECONDS = 20;
    private static final double BACKOFF_MULTIPLIER = 1.5;

    // ---- Logging thresholds ----
    private static final long SHORT_RUN_THRESHOLD  = TimeUnit.MINUTES.toMillis(30);
    private static final long MEDIUM_RUN_THRESHOLD = TimeUnit.HOURS.toMillis(2);
    private static final long LOG_INTERVAL_SHORT   = TimeUnit.MINUTES.toMillis(1);
    private static final long LOG_INTERVAL_MEDIUM  = TimeUnit.MINUTES.toMillis(3);
    private static final long LOG_INTERVAL_LONG    = TimeUnit.MINUTES.toMillis(5);

    public RunMonitor(LreRunApiClient client) {
        this.client = Objects.requireNonNull(client, "LreRunApiClient must not be null");
    }

    public RunState monitor(int runId,
                            PostRunAction postRunAction,
                            LreRunConfig runConfig) {

        validate(runConfig);

        log.info("Starting LRE run monitor → runId: [{}], postRunAction: [{}], expected duration: [{}h {}m]",
                runId,
                postRunAction.value(),
                runConfig.timeslotHours(),
                runConfig.timeslotMinutes());

        final long startTime = System.currentTimeMillis();
        final long timeoutMs = TimeUnit.MINUTES.toMillis(runConfig.totalMinutes());

        long delaySeconds = INITIAL_DELAY_SECONDS;
        int consecutiveFailures = 0;

        RunState lastState = RunState.UNDEFINED;
        long lastLogTime = 0;

        while (!Thread.currentThread().isInterrupted()) {

            // ---- Hard timeout guard ----
            if (isTimeslotExceeded(startTime, timeoutMs)) {
                log.error("Run [{}] exceeded expected duration ({} minutes). Marking as TIMED_OUT.",
                        runId, runConfig.totalMinutes());
                return RunState.TIMED_OUT;
            }

            try {
                RunState currentState = client.getRunState(runId);

                // Reset retry/backoff after successful call
                consecutiveFailures = 0;
                delaySeconds = INITIAL_DELAY_SECONDS;

                lastLogTime = logStateIfNeeded(
                        currentState,
                        lastState,
                        lastLogTime,
                        startTime,
                        timeoutMs
                );

                lastState = currentState;

                if (postRunAction.isTerminal(currentState)) {
                    log.info("Run [{}] reached terminal state [{}] (PostRunAction: [{}])",
                            runId, currentState, postRunAction.value());
                    return currentState;
                }

                sleep(delaySeconds);
                delaySeconds = nextDelay(delaySeconds);

            } catch (LreClientException e) {
                consecutiveFailures = handleFailure(runId, consecutiveFailures, e);
            }
        }

        log.warn("Run monitor interrupted for runId [{}]. Returning last known state [{}]",
                runId, lastState);

        Thread.currentThread().interrupt();
        return lastState;
    }

    private void validate(LreRunConfig config) {
        if (config.totalMinutes() <= 0) {
            throw new IllegalArgumentException("Run duration must be greater than zero.");
        }
    }

    private int handleFailure(int runId,
                              int consecutiveFailures,
                              LreClientException e) {

        consecutiveFailures++;

        log.warn("Failed to fetch state for run [{}] (attempt {}/{}). Error: {}",
                runId,
                consecutiveFailures,
                MAX_RETRIES,
                e.getMessage(),
                e);

        if (consecutiveFailures >= MAX_RETRIES) {
            throw new LreClientException(
                    "Max retries reached for run [" + runId + "]",
                    e
            );
        }

        sleep(RETRY_DELAY_SECONDS);
        return consecutiveFailures;
    }

    private long logStateIfNeeded(RunState currentState,
                                  RunState lastState,
                                  long lastLogTime,
                                  long startTime,
                                  long timeoutMs) {

        long now = System.currentTimeMillis();
        long elapsed = now - startTime;

        if (shouldLog(currentState, lastState, lastLogTime, now, timeoutMs)) {

            long remaining = Math.max(0, timeoutMs - elapsed);

            log.info("Run state [{}] → elapsed: [{}] → remaining: [{}]",
                    currentState,
                    formatDuration(elapsed),
                    formatDuration(remaining));

            return now;
        }

        return lastLogTime;
    }

    private boolean shouldLog(RunState current,
                              RunState previous,
                              long lastLogTime,
                              long now,
                              long timeoutMs) {

        if (current != previous) {
            return true;
        }

        return now - lastLogTime >= getDynamicLogInterval(timeoutMs);
    }

    private long getDynamicLogInterval(long timeoutMs) {
        if (timeoutMs <= SHORT_RUN_THRESHOLD) {
            return LOG_INTERVAL_SHORT;
        }
        if (timeoutMs <= MEDIUM_RUN_THRESHOLD) {
            return LOG_INTERVAL_MEDIUM;
        }
        return LOG_INTERVAL_LONG;
    }

    private long nextDelay(long currentDelaySeconds) {
        long next = (long) (currentDelaySeconds * BACKOFF_MULTIPLIER);
        return Math.min(next, MAX_DELAY_SECONDS);
    }

    private boolean isTimeslotExceeded(long startTime, long timeoutMs) {
        return System.currentTimeMillis() - startTime > timeoutMs;
    }

    private void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.warn("Run monitor sleep interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    private String formatDuration(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return minutes + "m " + seconds + "s";
    }
}