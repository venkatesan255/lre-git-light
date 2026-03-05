package util.string;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.IntStream;

@UtilityClass
public final class TableFormatter {

    public static String format(String[][] data) {
        return format(null, data);
    }

    public static String format(String[] header, String[][] dataRows) {
        if (isEmpty(header) && isEmpty(dataRows)) return "";

        int columns = determineColumnCount(header, dataRows);
        int[] columnWidths = computeColumnWidths(columns, header, dataRows);

        String border = buildBorder(columnWidths);
        String rowFormat = buildRowFormat(columnWidths);

        return buildTable(header, dataRows, border, rowFormat);
    }

    private static String buildTable(String[] header,
                                     String[][] dataRows,
                                     String border,
                                     String rowFormat) {

        StringBuilder sb = new StringBuilder("\n");
        sb.append(border);

        appendHeader(sb, header, border, rowFormat);
        appendDataRows(sb, dataRows, rowFormat);

        sb.append(border);
        return sb.toString();
    }

    private static void appendHeader(StringBuilder sb,
                                     String[] header,
                                     String border,
                                     String rowFormat) {

        if (!isEmpty(header)) {
            sb.append(String.format(rowFormat, (Object[]) header));
            sb.append(border);
        }
    }

    private static void appendDataRows(StringBuilder sb,
                                       String[][] dataRows,
                                       String rowFormat) {

        if (!isEmpty(dataRows)) {
            for (String[] row : dataRows) {
                sb.append(String.format(rowFormat, (Object[]) row));
            }
        }
    }

    private static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    private static boolean isEmpty(String[][] arr) {
        return arr == null || arr.length == 0;
    }

    private static int determineColumnCount(String[] header,
                                            String[][] dataRows) {

        if (!isEmpty(header)) return header.length;
        return dataRows[0].length;
    }

    private static int[] computeColumnWidths(int columns,
                                             String[] header,
                                             String[][] dataRows) {

        return IntStream.range(0, columns)
                .map(i -> computeColumnWidth(i, header, dataRows))
                .toArray();
    }

    private static int computeColumnWidth(int columnIndex,
                                          String[] header,
                                          String[][] dataRows) {

        int headerWidth = getHeaderWidth(columnIndex, header);
        int dataWidth = getDataRowsWidth(columnIndex, dataRows);
        return Math.max(headerWidth, dataWidth);
    }

    private static int getHeaderWidth(int columnIndex, String[] header) {
        return !isEmpty(header)
                && columnIndex < header.length
                && header[columnIndex] != null
                ? header[columnIndex].length()
                : 0;
    }

    private static int getDataRowsWidth(int columnIndex, String[][] dataRows) {
        if (isEmpty(dataRows)) return 0;

        return Arrays.stream(dataRows)
                .filter(row -> columnIndex < row.length && row[columnIndex] != null)
                .mapToInt(row -> row[columnIndex].length())
                .max()
                .orElse(0);
    }

    private static String buildBorder(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) {
            sb.append("-".repeat(w + 2)).append("+");
        }
        return sb.append("\n").toString();
    }

    private static String buildRowFormat(int[] widths) {
        StringBuilder sb = new StringBuilder("|");
        for (int w : widths) {
            sb.append(" %-").append(w).append("s |");
        }
        return sb.append("\n").toString();
    }
}