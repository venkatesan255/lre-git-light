package config;

import config.source.ConfigSource;
import model.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record LreConfigurationLoader(ConfigSource source) {

    public LreTestRunModel load() {
        List<String> missingKeys = new ArrayList<>();

        String serverUrl = getRequired("lre.serverUrl", missingKeys);
        String gitToken = getRequired("lre.gitToken", missingKeys);
        String domain = getRequired("lre.domain", missingKeys);
        String project = getRequired("lre.project", missingKeys);

        if (!missingKeys.isEmpty()) throw new ConfigurationValidationException(missingKeys);

        LreConnection connection = LreConnection.builder()
                .serverUrl(serverUrl)
                .gitToken(new SecretValue(gitToken))
                .domain(domain)
                .project(project)
                .build();

        String defaultWorkspace = Path.of(System.getProperty("user.dir"), "artifacts").toString();

        LreRunConfig runConfig = LreRunConfig.builder()
                .timeslotHours(getInt("timeslot.duration.hours", 0))
                .timeslotMinutes(getInt("timeslot.duration.minutes", 30))
                .maxErrors(getLong("run.maxErrors", 5000))
                .maxFailedTransactions(getLong("run.maxFailedTransactions", 3000))
                .vudsMode(getBoolean("run.vudsMode", false))
                .vudsAmount(getInt("run.vudsAmount", 0))
                .workspace(getOptionalString("run.workspace", defaultWorkspace))
                .runTestFromGitLab(getBoolean("run.fromGitLab", true))
                .build();

        LreTest test = LreTest.builder()
                .testId(getInt("test.id", 0))
                .testName(getOptionalString("test.name", ""))
                .folderPath(getOptionalString("test.folderPath", ""))
                .testInstanceId(getInt("test.instanceId", 0))
                .build();

        return new LreTestRunModel(connection, runConfig, test);
    }

    private String getRequired(String key, List<String> missingKeys) {
        return source.get(key)
                .filter(v -> !v.isBlank())
                .orElseGet(() -> {
                    missingKeys.add(key);
                    return null;
                });
    }

    private String getOptionalString(String key, String defaultValue) {
        return source.get(key).orElse(defaultValue);
    }

    private int getInt(String key, int defaultValue) {
        return source.get(key).map(Integer::parseInt).orElse(defaultValue);
    }

    private long getLong(String key, long defaultValue) {
        return source.get(key).map(Long::parseLong).orElse(defaultValue);
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return source.get(key).map(Boolean::parseBoolean).orElse(defaultValue);
    }
}