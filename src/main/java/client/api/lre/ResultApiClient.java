package client.api.lre;

import client.api.builder.ResultUrlBuilder;
import client.base.BaseClient;
import client.model.connection.LreConnection;
import model.result.ResultsExtractionRequest;

public class ResultApiClient extends BaseClient {

    private final ResultUrlBuilder urlBuilder;

    public ResultApiClient(LreConnection connection) {
        super(connection);
        this.urlBuilder = new ResultUrlBuilder(connection.buildApiUrl());
    }

    public void extractResults(ResultsExtractionRequest request) {
        String path = urlBuilder.getExtractResultsUrl();
        post(path, request);
    }
}
