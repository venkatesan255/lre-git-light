package app;

import model.LreConnection;
import model.LreRunConfig;
import model.LreRunContext;
import config.LreConfigurationLoader;
import config.source.CompositeConfigSource;
import config.source.EnvConfigSource;
import config.source.JsonConfigSource;
import model.LreTest;

import java.nio.file.Path;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        LreRunContext context = loadContext();

        LreConnection connection = context.connection();
        LreRunConfig runConfig = context.runConfig();
        LreTest lreTest = context.test();

        int exitCode = new ServiceFactory(connection)
                .buildRunService()
                .executeRun(runConfig, lreTest);

        System.exit(exitCode);
    }

    private static LreRunContext loadContext() {
        Path configPath = Optional.ofNullable(System.getenv("CONFIG_FILE"))
                .map(Path::of)
                .orElse(Path.of("config.json"));

        return new LreConfigurationLoader(new CompositeConfigSource(
                new EnvConfigSource(),
                new JsonConfigSource(configPath)
        )).load();
    }
}