package util.cli;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class ConfigurationLogger {

    /**
     * Print usage information
     */
    public static void printUsage() {
        log.info("LRE GitLab Client - Usage:");
        log.info("Operations:");
        log.info("  run              Execute a test run");
        log.info("  sync             Sync project with LRE");
        log.info("  results          Extract and download test results");
        log.info("                    Required: --run-id <id>");
        log.info("  help             Show this help message");
        log.info("Examples:");
        log.info("  java -jar lre-client.jar run");
        log.info("  java -jar lre-client.jar sync");
        log.info("  java -jar lre-client.jar results --run-id 456");
        log.info("Environment Variables:");
        log.info("  CONFIG_FILE      Path to config.json (default: ./config.json)");
        log.info("  LRE_SERVER_URL   Override LRE server URL");
        log.info("  LRE_GIT_TOKEN    Override Git token");
        log.info("  LOG_LEVEL        Set log level (default: INFO)");
        log.info("");
    }

}