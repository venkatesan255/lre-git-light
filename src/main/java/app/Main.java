package app;

import config.*;
import config.source.CompositeConfigSource;
import config.source.ConfigSource;
import config.source.EnvConfigSource;
import config.source.JsonConfigSource;
import lombok.extern.slf4j.Slf4j;
import model.LreTestRunModel;
import service.LreClient;
import service.LreRunService;

import java.nio.file.Path;
import java.util.Optional;

@Slf4j
public class Main {

    public static void main(String[] args) {

        Path configPath = Optional.ofNullable(System.getenv("CONFIG_FILE"))
                .map(Path::of)
                .orElse(Path.of("config.json"));

        ConfigSource source = new CompositeConfigSource(
                new EnvConfigSource(),
                new JsonConfigSource(configPath)
        );

        LreConfigurationLoader loader = new LreConfigurationLoader(source);
        LreTestRunModel model = loader.load();

        log.info(model.connection().toString());
        log.info(model.test().toString());
        log.info(model.runConfig().toString());

        LreClient client = new LreClient(model.connection());
        LreRunService runService = new LreRunService(client);

        int exitCode = runService.executeRun(model);

        System.exit(exitCode);
    }
}