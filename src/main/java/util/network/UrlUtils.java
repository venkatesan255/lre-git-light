package util.network;

import exception.LreClientException;
import lombok.experimental.UtilityClass;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@UtilityClass
public final class UrlUtils {

    /**
     * Joins a base path with resource segments, properly encoding each segment
     */
    public static String path(String base, Object... segments) {
        if (base == null) {
            throw new IllegalArgumentException("Base URL cannot be null");
        }

        StringBuilder sb = new StringBuilder(base);
        for (Object segment : segments) {
            if (segment == null) {
                throw new IllegalArgumentException("Path segment cannot be null");
            }

            // Check last character instead of creating new String
            if (!sb.isEmpty() && sb.charAt(sb.length() - 1) != '/') {
                sb.append('/');
            }

            // Encode path segment to handle special characters
            String encoded = encodePathSegment(segment.toString());
            sb.append(encoded);
        }

        return sb.toString();
    }

    /**
     * Encodes a single path segment for safe URL usage
     */
    private static String encodePathSegment(String segment) {
        // URLEncoder encodes for query params (space -> +), but we need path encoding (space -> %20)
        // So we use URLEncoder then replace + with %20
        String encoded = URLEncoder.encode(segment, StandardCharsets.UTF_8);
        return encoded.replace("+", "%20");
    }

    /**
     * Thin delegator for base/resource
     */
    public static String path(String base, String resource) {
        return path(base, (Object) resource);
    }

    public static String withQuery(String baseUrl, String key, String value) {
        if(baseUrl == null || key == null) throw new IllegalArgumentException("Base URL and key cannot be null");

        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.addParameter(key, value);
            return builder.build().toString();
        } catch (URISyntaxException e) {
            throw new LreClientException("Invalid URL: " + baseUrl, e);
        }
    }
}