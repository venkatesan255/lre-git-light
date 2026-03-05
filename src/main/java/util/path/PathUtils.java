package util.path;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtils {

    private static final String ROOT_FOLDER = "Subject";

    /**
     * Normalizes path separators to Unix-style forward slashes.
     * Removes leading/trailing slashes and collapses multiple slashes.
     *
     * @param path the path to normalize
     * @return normalized path with Unix separators, or empty string if input is null/blank
     */
    public static String normalizeSlashesToUnix(String path) {
        if (path == null || path.isBlank()) return "";

        return path.replace("\\", "/")
                .replaceAll("/+", "/")
                .replaceAll("^/+", "")
                .replaceAll("/+$", "");
    }

    /**
     * Normalizes a path to Unix-style format and ensures it starts with the root folder.
     *
     * <p>
     * This method performs the following operations:
     * <ul>
     *   <li>Converts all path separators to Unix-style forward slashes</li>
     *   <li>Removes leading and trailing slashes</li>
     *   <li>Collapses multiple consecutive slashes into a single slash</li>
     *   <li>Prepends "Subject/" if the path doesn't already start with it (case-insensitive check)</li>
     * </ul>
     *
     * @param path the path to normalize; may be null or blank
     * @return normalized Unix-style path starting with "Subject/", or "Subject" if input is null/blank/empty
     * @see #normalizeSlashesToUnix(String)
     */
    public static String normalizeToUnixPath(String path) {
        if (path == null || path.isBlank()) return ROOT_FOLDER;

        String normalized = normalizeSlashesToUnix(path);
        if (normalized.isEmpty()) return ROOT_FOLDER;

        if (!normalized.toLowerCase().startsWith("subject/") && !normalized.equalsIgnoreCase("subject")) {
            normalized = ROOT_FOLDER + "/" + normalized;
        }

        return normalized;
    }

    /**
     * Normalizes a path to Windows-style format with backslash separators.
     *
     * <p>
     * This method first normalizes the path to Unix format (ensuring it starts with "Subject/"),
     * then converts all forward slashes to backslashes for Windows compatibility.
     *
     * @param path the path to normalize; may be null or blank
     * @return normalized Windows-style path starting with "Subject\", or "Subject" if input is null/blank/empty
     * @see #normalizeToUnixPath(String)
     */
    public static String normalizeToWindowsPath(String path) {
        String unixPath = normalizeToUnixPath(path);
        return unixPath.replace("/", "\\");
    }
}