package client.model;

import client.model.connection.LreConnection;
import client.model.resultextraction.ResultNotification;
import client.model.run.RunConfig;
import client.model.sync.SyncConfig;
import client.model.test.LreTest;

public record LreRunContext(
        LreConnection connection,
        RunConfig runConfig,
        LreTest test,
        ResultNotification resultNotification,
        SyncConfig syncConfig
) {
}