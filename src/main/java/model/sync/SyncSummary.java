package model.sync;

public record SyncSummary(
        int uploaded,
        int deleted,
        int unchanged,
        int failed
) {

}
