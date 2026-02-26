package service;

import lombok.extern.slf4j.Slf4j;
import model.LreRunConfig;
import model.LreTest;

@Slf4j
public class LreRunService {

    private final LreTestManager testManager;

    public LreRunService(LreTestManager testManager) {
        this.testManager = testManager;
    }

    public int executeRun(LreRunConfig runConfig, LreTest testInput) {
        LreTest resolvedTest = testManager.resolveTest(testInput);
        log.info("Resolved test: id={}, name={}, path={}",
                resolvedTest.testId(), resolvedTest.testName(), resolvedTest.folderPath());

        // TODO: use runConfig to execute the run
        return 0;
    }
}