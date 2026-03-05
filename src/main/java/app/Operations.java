package app;

import client.api.lre.TestApiClient;
import client.model.LreRunContext;
import client.model.run.TestExecutionResult;
import client.model.test.LreTest;
import exception.LreClientException;
import exception.http.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import model.sync.SyncResponse;
import model.test.Test;
import service.ResultExtractionService;
import service.RunService;
import service.SyncService;
import service.lre.test.TestManager;
import util.cli.ArgumentParser;
import util.cli.ConfigurationLogger;

@Slf4j
public class Operations {

    private static final int EXIT_CODE_SUCCESS = 0;
    private static final int EXIT_LRE_FAILURE = 4;
    private static final int EXIT_INVALID_ARGS = 5;
    private static final int INVALID_RUN_ID = 0;

    private static final String OPERATION_RUN = "run";
    private static final String OPERATION_SYNC = "sync";
    private static final String OPERATION_RESULTS = "results";
    private static final String OPERATION_HELP = "help";

    public int executeOperation(String operation, String[] args, LreRunContext ctx) {
        log.debug("Connection: {}", ctx.connection());

        return switch (operation) {
            case OPERATION_RUN -> executeRun(ctx);

            case OPERATION_SYNC -> executeSync(ctx);

            case OPERATION_RESULTS -> {
                try {
                    int runId = ArgumentParser.parseRunId(args);
                    yield executeResults(runId, ctx, null);
                } catch (IllegalArgumentException e) {
                    log.error("Invalid run-id argument: {}", e.getMessage());
                    yield EXIT_INVALID_ARGS;
                }
            }

            case OPERATION_HELP -> {
                ConfigurationLogger.printUsage();
                yield EXIT_CODE_SUCCESS;
            }

            default -> {
                log.error("Unknown operation: {}", operation);
                ConfigurationLogger.printUsage();
                yield EXIT_INVALID_ARGS;
            }
        };
    }

    public int executeRun(LreRunContext ctx) {
        if (!ctx.runConfig().runTestFromGitLab()) {
            log.warn("Run Test from GitLab flag is disabled");
            return EXIT_CODE_SUCCESS;
        }

        ServiceFactory serviceFactory = new ServiceFactory(ctx.connection());
        TestManager testManager = serviceFactory.buildTestManager();
        TestApiClient testApiClient = serviceFactory.buildTestApiClient();

        try {
            LreTest resolvedTest = testManager.resolveTest(ctx.test());
            Test testInfo = testApiClient.findTestById(resolvedTest.testId());

            log.info("Test details retrieved successfully: {} (ID: {})",
                    resolvedTest.testName(), testInfo.getId());

            RunService runService = serviceFactory.buildRunService();
            TestExecutionResult result = runService.executeRun(ctx.runConfig(), resolvedTest);

            if (result.getRunId() <= INVALID_RUN_ID) {
                log.error("Run could not be started for test: {}", resolvedTest.testName());
                return EXIT_LRE_FAILURE;
            }

            if (result.isTestFailed()) {
                log.warn("Run [{}] completed with failure: {}",
                        result.getRunId(), result.getFailureReason());
            }

            log.info("Starting results extraction for run ID: {}", result.getRunId());
            int resultsExitCode = executeResults(result.getRunId(), ctx, testInfo, serviceFactory);

            if (resultsExitCode != EXIT_CODE_SUCCESS) {
                log.error("Results extraction failed for run ID: {}", result.getRunId());
                return EXIT_LRE_FAILURE;
            }

            log.info("Run and results extraction completed successfully");
            return EXIT_CODE_SUCCESS;

        } catch (NotFoundException e) {
            log.error("Test not found in LRE: {}", e.getMessage());
            return EXIT_LRE_FAILURE;
        } catch (Exception e) {
            log.error("Failed to execute run: {}", e.getMessage(), e);
            return EXIT_LRE_FAILURE;
        }
    }

    public int executeSync(LreRunContext ctx) {
        if (!ctx.syncConfig().syncEnabled()) {
            log.warn("Sync flag is disabled");
            return EXIT_CODE_SUCCESS;
        }

        log.info("Executing SYNC operation");

        try {
            log.info("Syncing project: {}/{}",
                    ctx.connection().domain(),
                    ctx.connection().project());

            ServiceFactory serviceFactory = new ServiceFactory(ctx.connection());
            SyncService syncService = serviceFactory.buildSyncService();
            SyncResponse response = syncService.performSync();

            if (!response.success()) {
                log.error("Sync operation returned unsuccessful status");
                return EXIT_LRE_FAILURE;
            }

            if (response.summary().failed() > 0) {
                log.warn("Sync completed with {} failures", response.summary().failed());
                return EXIT_LRE_FAILURE;
            }

            log.info("Sync completed successfully");
            return EXIT_CODE_SUCCESS;

        } catch (LreClientException e) {
            log.error("Sync operation failed: {}", e.getMessage(), e);
            return EXIT_LRE_FAILURE;
        }
    }

    public int executeResults(int runId, LreRunContext ctx, Test testInfo) {
        return executeResults(runId, ctx, testInfo, new ServiceFactory(ctx.connection()));
    }

    private int executeResults(int runId, LreRunContext ctx, Test testInfo, ServiceFactory serviceFactory) {
        log.info("Executing RESULTS operation for run ID: {}", runId);

        ResultExtractionService resultExtractionService =
                serviceFactory.buildResultExtractionService();

        try {
            resultExtractionService.extractResults(runId, ctx, testInfo);
            log.info("Results extraction completed successfully");
            return EXIT_CODE_SUCCESS;

        } catch (Exception e) {
            log.error("Results extraction failed for run ID: {}", runId, e);
            return EXIT_LRE_FAILURE;
        }
    }
}