package model;

import lombok.Builder;

@Builder
public record LreTest(int testId, String testName, String folderPath, int testInstanceId) {

    public boolean hasTestId() {
        return testId > 0;
    }
}