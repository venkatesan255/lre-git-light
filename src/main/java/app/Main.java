package app;

import config.ConfigurationValidationException;
import config.source.EnvConfigSource;
import config.source.JsonConfigSource;
import lombok.extern.slf4j.Slf4j;
import client.model.LreRunContext;
import config.LreConfigurationLoader;
import config.source.CompositeConfigSource;
import util.cli.ConfigurationLogger;
import util.io.LogHelper;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class Main {

    private static final int EXIT_CONFIG_ERROR = 1;
    private static final int EXIT_UNHANDLED_FAILURE = 2;
    private static final int EXIT_INVALID_ARGS = 3;

    public static void main(String[] args) {

        setupLogging();

        try {

            // Parse operation from arguments
            if (args.length == 0) {
                log.info("No arguments provided. see the available options below");
                ConfigurationLogger.printUsage();
                System.exit(EXIT_INVALID_ARGS);
            }

            String operation = args[0].toLowerCase();
            String[] operationArgs = Arrays.copyOfRange(args, 1, args.length);

            log.info("Starting LRE GitLab Client - Operation: {}", operation);

            LreRunContext model = loadContext();

            // Execute operation
            Operations operations = new Operations();
            int exitCode = operations.executeOperation(operation, operationArgs, model);

            log.info("Operation completed with exit code: {}", exitCode);
            System.exit(exitCode);

        } catch (ConfigurationValidationException e) {
            log.error("Configuration error: {}", e.getMessage());
            System.exit(EXIT_CONFIG_ERROR);
        } catch (Exception e) {
            log.error("Unhandled error", e);
            System.exit(EXIT_UNHANDLED_FAILURE);
        }
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

    private static void setupLogging() {
        String logLevel = System.getenv().getOrDefault("LOG_LEVEL", "INFO");
        LogHelper.setup(logLevel, true);
    }
}