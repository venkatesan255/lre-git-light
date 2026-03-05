package util.cli;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArgumentParser {

    /**
     * Parse run ID from command line arguments
     *
     * @param args command line arguments
     * @return run ID value
     * @throws IllegalArgumentException if --run-id is missing or invalid
     */
    public static int parseRunId(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("--run-id".equals(args[i]) && i + 1 < args.length) {
                try {
                    int runId = Integer.parseInt(args[i + 1]);
                    if (runId <= 0) {
                        throw new IllegalArgumentException(
                            "Run ID must be a positive integer, got: " + runId
                        );
                    }
                    return runId;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                        "Invalid run ID format: " + args[i + 1]
                    );
                }
            }
        }

        throw new IllegalArgumentException("Missing required argument: --run-id");
    }
}