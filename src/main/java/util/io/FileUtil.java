package util.io;

import exception.LreException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@UtilityClass
public class FileUtil {

    private static final String LRE_LOG_FILE = "lre_actions_%s.log";
    private static final String DEFAULT_OUTPUT_DIR = new File(System.getProperty("user.dir")).getAbsolutePath();

    public static String createLogFilename() {
        LocalDate today = LocalDate.now();
        String logFilename = String.format(LRE_LOG_FILE, today.format(DateTimeFormatter.ISO_DATE));
        Path logDirPath = Path.of(DEFAULT_OUTPUT_DIR, "artifacts");

        try {
            Files.createDirectories(logDirPath);
        } catch (IOException e) {
            throw new LreException("Failed to create directory for log file: " + logDirPath, e);
        }
        return logDirPath.resolve(logFilename).toAbsolutePath().toString();
    }
}
