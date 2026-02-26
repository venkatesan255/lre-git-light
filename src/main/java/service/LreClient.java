package service;

import model.LreConnection;

public class LreClient {

    private final LreConnection connection;

    public LreClient(LreConnection connection) {
        this.connection = connection;
    }

    public void authenticate() {
        // REST call logic here
    }
}