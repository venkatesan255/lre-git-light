package service.lre.monitor;

import client.api.lre.RunApiClient;
import client.model.run.RunConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.enums.RunState;
import model.run.RunStatusExtended;
import model.run.RunStatusExtendedWeb;
import util.datetime.DateTimeUtils;

@Slf4j
@RequiredArgsConstructor
public class RunStatusMonitor {

    private final RunApiClient runApiClient;
    private final RunConfig runConfig;
    private final int runId;

    public RunStatusExtended fetchCurrentStatus() {
        return runApiClient.getRunStatus(runId);
    }

    public boolean shouldAbortDueToErrors(RunStatusExtended currentStatus, RunState currentState) {
        if (currentState != RunState.RUNNING) return false;

        ThresholdCheck check = checkThresholds(currentStatus);

        if (check.isErrorLimitExceeded() || check.isFailedTxnLimitExceeded()) {
            log.warn("Run [{}] threshold breach detected. Current Errors: {}/{} | Failed Transactions: {}/{}",
                    runId, check.errorCount, check.errorThreshold,
                    check.failedTxnCount, check.failedTxnThreshold);
            return true;
        }

        return false;
    }

    public void abortRunDueToErrors(RunStatusExtended currentStatus) {
        ThresholdCheck check = checkThresholds(currentStatus);
        String reason = check.buildReason();

        log.error("Run [{}] Aborting test execution due to threshold breach: {}",
                runId, reason);

        try {
            runApiClient.abortRun(runId);
            log.warn("Run [{}] aborted successfully due to threshold breach", runId);
        } catch (Exception ex) {
            log.error("Failed to abort run [{}] after threshold breach: {}",
                    runId, ex.getMessage());
        }
    }

    public RunStatusExtendedWeb fetchRunStatusExtended() {
        RunStatusExtendedWeb res = runApiClient.getRunStatusWeb(runId);

        // API returns transPerSec but it is unreliable — recalculate manually
        long totalTransactions = res.getTransPassed();
        String duration = DateTimeUtils.calculateTestDuration(res.getStart(), res.getEnd());
        int tps = calculateTps(totalTransactions, duration);
        res.setTransPerSec(tps);
        return res;
    }

    private ThresholdCheck checkThresholds(RunStatusExtended currentStatus) {
        return new ThresholdCheck(
                currentStatus.getTotalErrors(),
                runConfig.maxErrors(),
                currentStatus.getTotalFailedTransactions(),
                runConfig.maxFailedTransactions()
        );
    }

    private int calculateTps(long totalTransactions, String durationHms) {
        if (durationHms == null || durationHms.isBlank()) {
            log.warn("Invalid duration for TPS calculation: {}", durationHms);
            return 0;
        }

        long durationSeconds = DateTimeUtils.parseHmsToSeconds(durationHms);
        if (durationSeconds == 0) return 0;

        double tps = (double) totalTransactions / durationSeconds;
        return (int) Math.round(tps);
    }

    private record ThresholdCheck(
            long errorCount,
            long errorThreshold,
            long failedTxnCount,
            long failedTxnThreshold
    ) {

        boolean isErrorLimitExceeded() {
            return errorThreshold > 0 && errorCount >= errorThreshold;
        }

        boolean isFailedTxnLimitExceeded() {
            return failedTxnThreshold > 0 && failedTxnCount >= failedTxnThreshold;
        }

        String buildReason() {
            StringBuilder reason = new StringBuilder();

            if (isErrorLimitExceeded()) {
                reason.append(String.format("Error threshold breached (%d/%d)",
                        errorCount, errorThreshold));
            }

            if (isFailedTxnLimitExceeded()) {
                if (!reason.isEmpty()) reason.append(" | ");
                reason.append(String.format("Failed Txn threshold breached (%d/%d)",
                        failedTxnCount, failedTxnThreshold));
            }

            return reason.toString();
        }
    }
}