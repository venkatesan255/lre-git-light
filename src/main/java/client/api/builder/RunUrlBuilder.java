package client.api.builder;

import util.network.UrlUtils;

/**
 * URL builder for run-related endpoints
 */
public record RunUrlBuilder(String baseUrl) {

    // Create Run
    // http://localhost:8080/api/lre/domains/test/projects/test/runs
    // Get Run Status
    // http://localhost:8080/api/lre/domains/test/projects/test/runs/1/status
    // Get Run Status extended
    // http://localhost:8080/api/lre/domains/test/projects/test/runs/1/status/extended
    // Abort Run
    // http://localhost:8080/api/lre/domains/test/projects/test/runs/1/abort
    // Get Run Results
    // http://localhost:8080/api/lre/domains/test/projects/test/runs/1/results
    // Get Run Results by ID
    // http://localhost:8080/api/lre/domains/test/projects/test/runs/1/results/1
    // Get Run Event Logs
    // http://localhost:8080/api/lre/domains/test/projects/test/runs/1/eventlog
    // Download Run Result File
    // http://localhost:8080/api/lre/domains/test/projects/test/runs/1/results/1/download-ui

    private static final String LRE_API_RUNS = "runs";
    private static final String LRE_API_RESULTS = "results";

    /**
     * Builds URL to create a new run
     */
    public String createRun() {
        return UrlUtils.path(baseUrl, LRE_API_RUNS);
    }

    /**
     * Builds URL to get run status
     */
    public String getRunStatus(int runId) {
        return UrlUtils.path(baseUrl, LRE_API_RUNS, runId, "status");
    }

    /**
     * Builds URL to get extended run status
     */
    public String getRunStatusExtended(int runId) {
        return UrlUtils.path(baseUrl, LRE_API_RUNS, runId, "status", "extended");
    }

    /**
     * Builds URL to abort a run
     */
    public String abortRun(int runId) {
        return UrlUtils.path(baseUrl, LRE_API_RUNS, runId, "abort");
    }

    /**
     * Builds URL to get all run results
     */
    public String getRunResults(int runId) {
        return UrlUtils.path(baseUrl, LRE_API_RUNS, runId, LRE_API_RESULTS);
    }
}