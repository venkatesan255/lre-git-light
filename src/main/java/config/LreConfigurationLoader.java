package config;

import client.model.LreRunContext;
import client.model.connection.LreConnection;
import client.model.connection.SecretValue;
import client.model.resultextraction.ResultNotification;
import client.model.run.RunConfig;
import client.model.sync.SyncConfig;
import client.model.test.LreTest;
import config.source.ConfigSource;
import config.source.ConfigSourceReader;
import model.enums.PostRunAction;
import model.timeslot.Timeslot;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class LreConfigurationLoader {

    private final ConfigSourceReader reader;

    public LreConfigurationLoader(ConfigSource source) {
        this.reader = new ConfigSourceReader(source);
    }

    public LreRunContext load() {
        List<String> missingKeys = new ArrayList<>();
        LreConnection connection = buildConnection(missingKeys);

        if (!missingKeys.isEmpty()) {
            throw new ConfigurationValidationException(missingKeys);
        }

        return new LreRunContext(
                connection,
                buildRunConfig(),
                buildTest(),
                buildNotification(),
                buildSyncConfig()
        );
    }

    private LreConnection buildConnection(List<String> missingKeys) {
        return LreConnection.builder()
                .serverUrl(reader.getRequired("lre.serverUrl", missingKeys))
                .gitToken(new SecretValue(reader.getRequired("lre.gitToken", missingKeys)))
                .domain(reader.getRequired("lre.domain", missingKeys))
                .project(reader.getRequired("lre.project", missingKeys))
                .build();
    }

    private RunConfig buildRunConfig() {
        Timeslot timeslot = Timeslot.builder()
                .hours(reader.getInt("timeslot.duration.hours", 0))
                .minutes(reader.getInt("timeslot.duration.minutes", 30))
                .build();

        String defaultWorkspace = Path.of(System.getProperty("user.dir"), "artifacts").toString();

        return RunConfig.builder()
                .timeslot(timeslot)
                .maxErrors(reader.getLong("run.maxErrors", 5000))
                .maxFailedTransactions(reader.getLong("run.maxFailedTransactions", 3000))
                .workspace(reader.getOptionalString("run.workspace", defaultWorkspace))
                .runTestFromGitLab(reader.getBoolean("run.fromGitLab", true))
                .postRunAction(PostRunAction.fromString(reader.getOptionalString("run.postRunAction", "Collate and Analyze")))
                .build();
    }

    private LreTest buildTest() {
        return LreTest.builder()
                .testId(reader.getInt("test.id", 0))
                .testName(reader.getOptionalString("test.name", ""))
                .folderPath(reader.getOptionalString("test.folderPath", ""))
                .instanceId(reader.getInt("test.instanceId", 0))
                .build();
    }

    private ResultNotification buildNotification() {
        return ResultNotification.builder()
                .runId(reader.getInt("notification.runId", 0))
                .ccRecipients(reader.getStringList("notification.cc"))
                .bccRecipients(reader.getStringList("notification.bcc"))
                .build();
    }

    private SyncConfig buildSyncConfig() {
        return SyncConfig.builder()
                .syncEnabled(reader.getBoolean("sync.fromGitLab", true))
                .build();
    }
}