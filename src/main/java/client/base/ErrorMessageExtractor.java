package client.base;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessageExtractor {

    public static String extract(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) return null;

        try {
            int messageIndex = responseBody.indexOf("\"message\":\"");
            if (messageIndex == -1) return null;

            int startIndex = messageIndex + 11;
            int endIndex = responseBody.indexOf("\"", startIndex);
            if (endIndex == -1) return null;

            return responseBody.substring(startIndex, endIndex)
                    .replace("\\r\\n", " ")
                    .replace("\\n", " ")
                    .replace("\\\"", "\"");
        } catch (Exception e) {
            return null;
        }
    }
}