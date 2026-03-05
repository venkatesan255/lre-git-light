package service;

import client.api.lre.SyncApiClient;
import exception.LreClientException;
import exception.http.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.sync.CategorizedChanges;
import model.sync.ScriptChange;
import model.sync.SyncResponse;
import model.sync.SyncSummary;
import util.string.StringFormatUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class SyncService {

    private final SyncApiClient syncApiClient;

    public SyncResponse performSync() {
        log.info("Starting sync operation");

        try {
            SyncResponse response = syncApiClient.sync();
            logSyncResults(response);
            return response;

        } catch (ForbiddenException e) {
            log.error("Sync operation failed due to access issue: {}", e.getMessage());
            throw new LreClientException("Insufficient permissions to sync project", e);

        } catch (Exception e) {
            log.error("Sync operation failed: {}", e.getMessage());
            throw new LreClientException("Failed to perform sync operation", e);
        }
    }

    private void logSyncResults(SyncResponse response) {
        SyncSummary summary = response.summary();
        CategorizedChanges changes = response.changes();

        log.info("Summary: Uploaded: {}, Deleted: {}, Unchanged: {}, Failed: {}",
                summary.uploaded(), summary.deleted(), summary.unchanged(), summary.failed());

        List<ScriptChange> allChanges = Stream.of(
                        changes.uploaded(),
                        changes.deleted(),
                        changes.unchanged(),
                        changes.failed())
                .flatMap(List::stream)
                .sorted(Comparator.comparing(ScriptChange::action))
                .toList();

        if (allChanges.isEmpty()) {
            log.info("No sync operations to display.");
            return;
        }

        logSyncTable(allChanges);
    }

    private void logSyncTable(List<ScriptChange> changes) {
        String[] header = {"#", "Test Folder Path", "Script Name", "Commit", "Action", "Status", "Message"};
        String[][] dataRows = new String[changes.size()][header.length];

        for (int i = 0; i < changes.size(); i++) {
            ScriptChange change = changes.get(i);
            dataRows[i] = new String[]{
                    String.valueOf(i + 1),
                    change.testFolderPath(),
                    change.scriptName(),
                    change.commitSha() != null ? change.commitSha() : "N/A",
                    change.action(),
                    change.status(),
                    change.message() != null ? change.message() : ""
            };
        }

        log.info("Sync operations ({} total):", changes.size());
        log.info(StringFormatUtils.logTable(header, dataRows));
    }
}