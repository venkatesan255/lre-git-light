package app;

import client.api.lre.ResultApiClient;
import client.api.lre.RunApiClient;
import client.api.lre.SyncApiClient;
import client.api.lre.TestApiClient;
import client.model.connection.LreConnection;
import service.ResultExtractionService;
import service.RunService;
import service.SyncService;
import service.lre.test.TestManager;

public class ServiceFactory {

    private final LreConnection connection;

    public ServiceFactory(LreConnection connection) {
        this.connection = connection;
    }

    // Cached clients
    private RunApiClient runApiClient() { return new RunApiClient(connection); }
    private TestApiClient testApiClient() { return new TestApiClient(connection); }
    private ResultApiClient resultApiClient() { return new ResultApiClient(connection); }

    public RunService buildRunService() {
        return new RunService(runApiClient());
    }

    public TestManager buildTestManager() {
        return new TestManager(testApiClient());
    }

    public SyncService buildSyncService() {
        return new SyncService(new SyncApiClient(connection));
    }

    public TestApiClient buildTestApiClient() {
        return testApiClient();
    }

    public ResultExtractionService buildResultExtractionService() {
        return new ResultExtractionService(
                resultApiClient(),
                testApiClient(),
                runApiClient()
        );
    }
}