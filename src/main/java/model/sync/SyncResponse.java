package model.sync;

public record SyncResponse(
        boolean success,
        SyncSummary summary,
        CategorizedChanges changes
) {
}
