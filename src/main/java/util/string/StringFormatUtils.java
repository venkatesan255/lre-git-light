package util.string;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class StringFormatUtils {

    public static String toTitleCase(String input) {
        String[] parts = input.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(Character.toUpperCase(parts[i].charAt(0)))
              .append(parts[i].substring(1));
        }

        return sb.toString();
    }

    public static String logTable(String[][] data) {
        return TableFormatter.format(data);
    }

    public static String logTable(String[] header, String[][] dataRows) {
        return TableFormatter.format(header, dataRows);
    }

    public static String buildProgressBar(int percent) {
        int barWidth = 20;
        int filledBlocks = (percent * barWidth) / 100;

        return "█".repeat(filledBlocks)
                + "░".repeat(barWidth - filledBlocks);
    }
}