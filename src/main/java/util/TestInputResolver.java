package util;

public class TestInputResolver {

    public enum TestType {
        BY_ID,      // "123"
        EXISTING,   // "Subject/TeamA/My Load Test"
        EXCEL       // "Subject/TeamA/myTest.xlsx"
    }

    /**
     * Detects the type of test input provided by the user.
     */
    public static TestType detect(String testValue) {
        if (testValue == null || testValue.isBlank()) return TestType.EXISTING;
        if (testValue.chars().allMatch(Character::isDigit)) return TestType.BY_ID;
        if (testValue.endsWith(".xlsx")) return TestType.EXCEL;
        return TestType.EXISTING;
    }

    /**
     * Extracts the test name from a full path.
     * "Subject/TeamA/My Load Test" → "My Load Test"
     */
    public static String extractName(String testPath) {
        String normalized = normalizePath(testPath);
        int lastSlash = normalized.lastIndexOf('/');
        return lastSlash == -1 ? normalized : normalized.substring(lastSlash + 1);
    }

    /**
     * Normalises a path to forward slashes, removes duplicates,
     * trims leading/trailing slashes, and ensures "Subject/" prefix.
     *
     * Examples:
     *   "Subject\\TeamA\\My Test"  → "Subject/TeamA/My Test"
     *   "TeamA/My Test"            → "Subject/TeamA/My Test"
     *   ""                         → "Subject"
     */
    public static String normalizePath(String path) {
        if (path == null || path.isBlank()) return "Subject";

        // Step 1: Normalize all backslashes to forward slashes
        String normalized = path.replace("\\", "/");

        // Step 2: Remove repeated slashes
        normalized = normalized.replaceAll("/++", "/");

        // Step 3: Trim leading slashes
        normalized = normalized.replaceAll("^/++", "");

        // Step 4: Trim trailing slashes
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        // Step 5: Ensure "Subject/" prefix (case-insensitive)
        if (!normalized.toLowerCase().startsWith("subject/")) {
            normalized = "Subject/" + normalized;
        }

        return normalized;
    }
}