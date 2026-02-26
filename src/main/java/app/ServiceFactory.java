package app;

import client.LreTestApiClient;
import model.LreConnection;
import service.LreRunService;
import service.LreTestManager;

public class ServiceFactory {

    private final LreConnection connection;

    public ServiceFactory(LreConnection connection) {
        this.connection = connection;
    }

    public LreRunService buildRunService() {
        return new LreRunService(buildTestManager());
    }

    private LreTestManager buildTestManager() {
        return new LreTestManager(new LreTestApiClient(connection));
    }
}