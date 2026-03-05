package service.lre.poller;

import model.enums.RunState;

import java.util.EnumMap;
import java.util.Map;

public class RunProgressCalculator {

    private final long timeslotDurationMillis;
    private final Map<RunState, StateProgressConfig> stateConfigs;

    public RunProgressCalculator(long timeslotDurationMillis) {
        this.timeslotDurationMillis = timeslotDurationMillis;
        this.stateConfigs = initializeStateConfigs();
    }

    private Map<RunState, StateProgressConfig> initializeStateConfigs() {
        Map<RunState, StateProgressConfig> configs = new EnumMap<>(RunState.class);

        configs.put(RunState.INITIALIZING,
                new StateProgressConfig(0.05, 0, 10));
        configs.put(RunState.RUNNING,
                new StateProgressConfig(0.85, 10, 85));
        configs.put(RunState.STOPPING,
                new StateProgressConfig(0.01, 85, 86));
        configs.put(RunState.BEFORE_COLLATING_RESULTS,
                new StateProgressConfig(0.01, 86, 87));
        configs.put(RunState.COLLATING_RESULTS,
                new StateProgressConfig(0.04, 87, 92));
        configs.put(RunState.BEFORE_CREATING_ANALYSIS_DATA,
                new StateProgressConfig(0.02, 92, 94));
        configs.put(RunState.CREATING_ANALYSIS_DATA,
                new StateProgressConfig(0.02, 94, 98));
        configs.put(RunState.FINISHED,
                new StateProgressConfig(0.0, 100, 100));

        return configs;
    }

    public int calculateProgress(RunState state, long totalElapsedMillis) {
        if (state == RunState.FINISHED) return 100;
        if (state == RunState.RUNNING) return calculateRunningProgress(totalElapsedMillis);
        return calculateStateProgress(state, totalElapsedMillis);
    }

    private int calculateRunningProgress(long totalElapsedMillis) {
        if (timeslotDurationMillis <= 0) return 10;

        int runningProgress =
                10 + (int) ((totalElapsedMillis * 75) / timeslotDurationMillis);

        return Math.min(runningProgress, 85);
    }

    private int calculateStateProgress(RunState state, long totalElapsedMillis) {
        StateProgressConfig config = stateConfigs.get(state);
        if (config == null) return 0;

        long stateAllocatedTime =
                (long) (timeslotDurationMillis * config.timeAllocation());

        long stateStartTime = calculateStateStartTime(state);
        long timeInState = Math.max(0, totalElapsedMillis - stateStartTime);

        int progressRange = config.endProgress() - config.startProgress();

        if (stateAllocatedTime > 0) {
            int stateInternalProgress =
                    (int) ((timeInState * progressRange) / stateAllocatedTime);

            return config.startProgress() +
                    Math.min(stateInternalProgress, progressRange);
        }

        return config.startProgress();
    }

    private long calculateStateStartTime(RunState state) {
        long cumulativeTime = 0;

        // EnumMap iterates in RunState enum declaration order.
        // States must be declared in execution sequence for cumulative time to be correct.
        for (Map.Entry<RunState, StateProgressConfig> entry : stateConfigs.entrySet()) {
            if (entry.getKey() == state) break;
            cumulativeTime += (long) (timeslotDurationMillis * entry.getValue().timeAllocation());
        }

        return cumulativeTime;
    }


    /**
     * Configuration for a single state's progress calculation
     */
    private record StateProgressConfig(
            double timeAllocation,  // Percentage of total timeslot (0.0 to 1.0)
            int startProgress,      // Progress percentage when entering state
            int endProgress         // Progress percentage when completing state
    ) {
    }
}