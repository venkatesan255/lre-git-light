package model;

import lombok.Builder;

@Builder
public record TestExecutionResult(int runId, boolean failed, String failureReason) {
}