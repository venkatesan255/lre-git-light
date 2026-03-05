package client.api.lre;

import client.api.builder.SyncUrlBuilder;
import client.base.BaseClient;
import client.model.connection.LreConnection;
import model.sync.SyncResponse;
import util.serialization.JsonUtils;

public class SyncApiClient extends BaseClient {

    private final SyncUrlBuilder urlBuilder;

    public SyncApiClient(LreConnection connection) {
        super(connection);
        this.urlBuilder = new SyncUrlBuilder(connection.buildApiUrl());
    }

    public SyncResponse sync() {
        String path = urlBuilder.getSyncUrl();
        return JsonUtils.readValue(post(path, "")
                .body(), SyncResponse.class);
    }
}
