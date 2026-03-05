package model.sync;

public record ScriptChange(
        String path,
        String scriptName,
        String commitSha,
        String action,
        String status,
        String message,
        String testFolderPath,
        Integer lreScriptId
) {
}
