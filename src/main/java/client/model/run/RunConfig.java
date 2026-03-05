package client.model.run;

import lombok.Builder;
import model.enums.PostRunAction;
import model.timeslot.Timeslot;

@Builder
public record RunConfig(
        boolean runTestFromGitLab,
        Timeslot timeslot,
        long maxErrors,
        long maxFailedTransactions,
        PostRunAction postRunAction,
        String workspace
) {
}
