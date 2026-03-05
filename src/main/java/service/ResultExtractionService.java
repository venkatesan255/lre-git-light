package service;

import client.api.lre.ResultApiClient;
import client.api.lre.RunApiClient;
import client.api.lre.TestApiClient;
import client.model.LreRunContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.result.ResultsExtractionRequest;
import model.run.RunStatusExtended;
import model.test.Test;

@Slf4j
@RequiredArgsConstructor
public class ResultExtractionService {

    private final ResultApiClient resultApiClient;
    private final TestApiClient testApiClient;
    private final RunApiClient runApiClient;

    public void extractResults(int runId, LreRunContext ctx, Test testInfo) {
        log.info(ctx.connection().toString());
        String outputDir = ctx.runConfig().workspace();
        log.info("Extracting results for run ID: {} to: {}", runId, outputDir);

        // Fetch test info if not provided
        Test resolvedTestInfo = resolveTestInfo(runId, testInfo);

        // Build and execute extraction request
        ResultsExtractionRequest request = buildResultExtractionRequest(runId, ctx, resolvedTestInfo);
        extractAndEmailToUser(request);

        log.info("Results extracted successfully: {}", resolvedTestInfo.getId());
    }

    private Test resolveTestInfo(int runId, Test testInfo) {
        if (testInfo == null) {
            log.info("Test info is not provided. Fetching it from LRE using run ID");
            RunStatusExtended runStatusExtended = runApiClient.getRunStatus(runId);
            return testApiClient.findTestById(runStatusExtended.getTestId());
        }
        return testInfo;
    }

    private ResultsExtractionRequest buildResultExtractionRequest(int runId, LreRunContext ctx, Test test) {
        return ResultsExtractionRequest.builder()
                .runId(runId)
                .maxErrors(ctx.runConfig().maxErrors())
                .maxFailedTransactions(ctx.runConfig().maxFailedTransactions())
                .test(test)
                .ccRecipients(ctx.resultNotification().ccRecipients())
                .bccRecipients(ctx.resultNotification().bccRecipients())
                .build();
    }

    private void extractAndEmailToUser(ResultsExtractionRequest request) {
        resultApiClient.extractResults(request);
    }
}