package util.datetime;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtils {

    public static String formatDateTime(LocalDateTime dt) {
        if (dt == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        return dt.format(formatter);
    }

    public static String calculateTestDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return "N/A";
        Duration duration = Duration.between(start, end);
        return String.format("%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }

    public static String formatDuration(long millis) {
        Duration d = Duration.ofMillis(millis);
        return String.format("%02d:%02d:%02d",
                d.toHoursPart(),
                d.toMinutesPart(),
                d.toSecondsPart());
    }

    public static long parseHmsToSeconds(String hms) {
        if (hms == null || hms.equals("N/A")) return 0;
        String[] parts = hms.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }
}