package model;

public record LreTestRunModel(
        LreConnection connection,
        LreRunConfig runConfig,
        LreTest test
) {
}