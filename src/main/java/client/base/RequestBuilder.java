package client.base;

import lombok.extern.slf4j.Slf4j;
import client.model.connection.LreConnection;
import org.apache.commons.lang3.StringUtils;
import util.serialization.JsonUtils;

import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;

@Slf4j
public class RequestBuilder {

    public static final String PRIVATE_TOKEN_HEADER = "PRIVATE-TOKEN";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";

    private final LreConnection connection;
    private final Duration requestTimeout;

    public RequestBuilder(LreConnection connection, Duration requestTimeout) {
        this.connection = connection;
        this.requestTimeout = requestTimeout;
    }

    public HttpRequest buildGet(String path) {
        validate(path);
        return baseRequest(path).GET().build();
    }

    public HttpRequest buildPost(String path, Object body) {
        validate(path);
        return baseRequest(path)
                .header(CONTENT_TYPE, JSON_CONTENT_TYPE)
                .POST(toPublisher(body))
                .build();
    }

    public HttpRequest buildPut(String path, Object body) {
        validate(path);
        return baseRequest(path)
                .header(CONTENT_TYPE, JSON_CONTENT_TYPE)
                .PUT(toPublisher(body))
                .build();
    }

    public HttpRequest buildDelete(String path) {
        validate(path);
        return baseRequest(path).DELETE().build();
    }

    public HttpRequest buildMultipart(String path, Path filePath, Object metadata) {
        validate(path);
        Objects.requireNonNull(filePath, "File path cannot be null");
        Objects.requireNonNull(metadata, "Metadata cannot be null");

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        HttpRequest.BodyPublisher bodyPublisher = bodyBuilder.build(filePath, metadata);

        return baseRequest(path)
                .header(CONTENT_TYPE, bodyBuilder.getContentTypeHeader())
                .POST(bodyPublisher)
                .build();
    }

    private HttpRequest.BodyPublisher toPublisher(Object body) {
        if (body == null) return HttpRequest.BodyPublishers.noBody();
        String json = JsonUtils.toJson(body);
        logRequestBody(json);
        return HttpRequest.BodyPublishers.ofString(json);
    }

    private HttpRequest.Builder baseRequest(String path) {
        return HttpRequest.newBuilder()
                .uri(buildUri(path))
                .header(PRIVATE_TOKEN_HEADER, connection.gitToken().value())
                .header("Accept", JSON_CONTENT_TYPE)
                .timeout(requestTimeout);
    }

    private URI buildUri(String path) {
        if (path.startsWith("http")) {
            return URI.create(path);
        }
        return URI.create(connection.buildApiUrl() + path);
    }

    private void logRequestBody(String json) {
        if (log.isDebugEnabled()) {
            log.debug("Request body: {}", StringUtils.truncate(json, 1000));
        }
    }

    private void validate(String path) {
        Objects.requireNonNull(path, "Path cannot be null");
        if (path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
    }
}