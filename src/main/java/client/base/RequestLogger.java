package client.base;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static client.base.RequestBuilder.PRIVATE_TOKEN_HEADER;

public class RequestLogger {

    private static final Logger log = LoggerFactory.getLogger(RequestLogger.class);

    public void logRequest(HttpRequest request) {
        if (!log.isDebugEnabled()) return;

        String method = request.method();
        String uri = sanitizeUri(request.uri());

        log.debug("-> {} {}", method, uri);

        request.headers().map().forEach((key, values) -> {
            String sanitizedValue = PRIVATE_TOKEN_HEADER.equalsIgnoreCase(key)
                    ? "***"
                    : String.join(", ", values);
            log.debug("   Header: {}: {}", key, sanitizedValue);
        });
    }

    public void logResponse(HttpRequest request, HttpResponse<String> response, long duration) {
        if (!log.isDebugEnabled()) return;

        String method = request.method();
        String uri = sanitizeUri(request.uri());
        int status = response.statusCode();

        log.debug("<- {} {} (status: {}, duration: {}ms)",
                method, uri, status, duration);

        String body = response.body();
        if (body != null && !body.isEmpty()) {
            String abbreviated = org.apache.commons.lang3.StringUtils.abbreviate(body, 500);
            log.debug("   Response body: {}", abbreviated);
        }
    }

    public void logInterruption(HttpRequest request, long duration) {
        if (!log.isErrorEnabled()) return;

        String method = request.method();
        String uri = sanitizeUri(request.uri());

        log.error("HTTP call interrupted after {}ms: {} {}",
                duration, method, uri);
    }

    public void logIOException(HttpRequest request, IOException e, long duration) {
        if (!log.isErrorEnabled()) return;

        String method = request.method();
        String uri = sanitizeUri(request.uri());
        String exceptionMsg = e.getMessage() != null
                ? e.getMessage()
                : e.getClass().getSimpleName();

        log.error("HTTP call failed {} {} after {}ms: {}", method, uri, duration, exceptionMsg);
    }

    private String sanitizeUri(URI uri) {
        String path = uri.getPath();
        return path != null ? path : uri.toString();
    }
}