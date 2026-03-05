package service.lre.poller;

import model.enums.RunState;
import util.datetime.DateTimeUtils;

public record RunStatusFormatter(int runId, long timeslotDurationMillis) {

    public String formatStatusLog(
            RunState state,
            long elapsedMillis,
            long remainingMillis,
            int progress,
            String progressBar
    ) {
        return String.format(
                "| %-10s | %-40s | %-25s | %-14s | %-14s | %-20s |",
                "RunId: " + runId,
                "State: " + state.getValue(),
                "Elapsed: " + DateTimeUtils.formatDuration(elapsedMillis),
                "Timeslot: " + DateTimeUtils.formatDuration(timeslotDurationMillis),
                "Time remaining: " + DateTimeUtils.formatDuration(remainingMillis),
                String.format("%3d%% %s", progress, progressBar)
        );
    }
}