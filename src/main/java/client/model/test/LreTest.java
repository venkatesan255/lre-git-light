package client.model.test;

import lombok.Builder;

@Builder
public record LreTest(int testId, String testName, String folderPath, int instanceId) {

    public boolean hasTestId() {
        return testId > 0;
    }
}