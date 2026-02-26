package model;

public record LreRunContext(
        LreConnection connection,
        LreRunConfig runConfig,
        LreTest test
) {
}