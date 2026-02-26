package service;

import model.LreTestRunModel;

public class LreRunService {

    private final LreClient client;

    public LreRunService(LreClient client) {
        this.client = client;
    }

    public int executeRun(LreTestRunModel model) {
        // trigger run via REST
        return 0;
    }
}