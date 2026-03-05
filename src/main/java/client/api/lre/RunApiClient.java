package client.api.lre;

import client.api.builder.RunUrlBuilder;
import client.base.BaseClient;
import client.model.connection.LreConnection;
import model.run.RunStartRequest;
import model.run.RunStartResponse;
import model.run.RunStatusExtended;
import model.run.RunStatusExtendedWeb;
import util.serialization.JsonUtils;

public class RunApiClient extends BaseClient {

    private final RunUrlBuilder urlBuilder;

    public RunApiClient(LreConnection connection) {
        super(connection);
        this.urlBuilder = new RunUrlBuilder(connection.buildApiUrl());
    }

    public RunStartResponse createRun(RunStartRequest runStartRequest) {
        String path = urlBuilder.createRun();
        return JsonUtils.readValue(post(path, runStartRequest).body(), RunStartResponse.class);
    }

    public void abortRun(int runId) {
        String path = urlBuilder.abortRun(runId);
        post(path, null);
    }

    public RunStatusExtended getRunStatus(int runId) {
        String path = urlBuilder.getRunStatus(runId);
        return JsonUtils.readValue(get(path).body(), RunStatusExtended.class);
    }

    public RunStatusExtendedWeb getRunStatusWeb(int runId) {
        String path = urlBuilder.getRunStatusExtended(runId);
        return JsonUtils.readValue(get(path).body(), RunStatusExtendedWeb.class);
    }
}