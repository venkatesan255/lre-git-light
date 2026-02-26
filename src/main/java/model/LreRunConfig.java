package model;

import lombok.Builder;

@Builder
public record LreRunConfig(
        int timeslotHours,
        int timeslotMinutes,
        long maxErrors,
        long maxFailedTransactions,
        boolean vudsMode,
        int vudsAmount,
        String workspace,
        boolean runTestFromGitLab

) {
    public int totalMinutes() {
        return (timeslotHours * 60) + timeslotMinutes;
    }
}