package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.LreConnection;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;

public abstract class BaseClient {

    protected final LreConnection connection;
    protected final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String baseUrl;

    protected BaseClient(LreConnection connection) {
        this.connection = connection;
        this.baseUrl = connection.buildApiUrl();

        this.objectMapper = new ObjectMapper()
                .findAndRegisterModules();

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }


    protected HttpResponse<String> get(String path) {
        HttpRequest request = baseRequest(path)
                .GET()
                .build();
        return send(request);
    }

    protected HttpResponse<String> post(String path, Object body) {
        String json = serialize(body);

        HttpRequest request = baseRequest(path)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return send(request);
    }

    protected HttpResponse<String> put(String path, Object body) {
        String json = serialize(body);

        HttpRequest request = baseRequest(path)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return send(request);
    }

    protected HttpResponse<String> delete(String path) {
        HttpRequest request = baseRequest(path)
                .DELETE()
                .build();
        return send(request);
    }

    protected HttpResponse<String> postMultipart(
            String path,
            Path filePath,
            Object metadata) throws IOException {

        String boundary = "----LREBoundary" + UUID.randomUUID();
        String metadataJson = serialize(metadata);
        String safeFilename = filePath.getFileName()
                .toString()
                .replace("\"", "");

        String metadataPart =
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"metadata\"\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        metadataJson + "\r\n";

        String filePartHeader =
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"file\"; filename=\"" + safeFilename + "\"\r\n" +
                        "Content-Type: " + detectContentType(filePath) + "\r\n\r\n";

        String closingBoundary =
                "\r\n--" + boundary + "--\r\n";

        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.concat(
                HttpRequest.BodyPublishers.ofString(metadataPart, StandardCharsets.UTF_8),
                HttpRequest.BodyPublishers.ofString(filePartHeader, StandardCharsets.UTF_8),
                HttpRequest.BodyPublishers.ofFile(filePath),
                HttpRequest.BodyPublishers.ofString(closingBoundary, StandardCharsets.UTF_8)
        );

        HttpRequest request = baseRequest(path)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(bodyPublisher)
                .build();

        return send(request);
    }

    /* =========================
       Core send logic
       ========================= */

    private HttpResponse<String> send(HttpRequest request) {
        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            handleError(response);

            return response;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LreClientException("HTTP call interrupted: " + request.uri(), e);
        } catch (IOException e) {
            throw new LreClientException("HTTP call failed: " + request.uri(), e);
        }
    }

    /* =========================
       Request builder
       ========================= */

    private HttpRequest.Builder baseRequest(String path) {
        return HttpRequest.newBuilder()
                .uri(buildUri(path))
                .header("Private-Token", connection.gitToken().value())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60));
    }

    private URI buildUri(String path) {
        if (path.startsWith("http")) return URI.create(path);
        return URI.create(baseUrl + path);
    }

    /* =========================
       Error handling
       ========================= */

    private void handleError(HttpResponse<String> response) {
        int status = response.statusCode();

        if (status >= 200 && status < 300) return;

        switch (status) {
            case 401 -> throw new LreClientException(
                    "Unauthorized — check your Private-Token");
            case 403 -> throw new LreClientException(
                    "Forbidden — insufficient permissions");
            case 404 -> throw new LreClientException(
                    "Resource not found: " + response.uri());
            case 500 -> throw new LreClientException(
                    "Server error: " + response.body());
            default -> throw new LreClientException(
                    "Unexpected response " + status + ": " + response.body());
        }
    }

    /* =========================
       JSON Helpers
       ========================= */

    protected <T> T readValue(String body, Class<T> clazz) {
        try {
            return objectMapper.readValue(body, clazz);
        } catch (IOException e) {
            throw new LreClientException("Failed to parse JSON response", e);
        }
    }

    private String serialize(Object body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new LreClientException("Failed to serialize request body", e);
        }
    }

    protected <T> T readValue(String body, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(body, typeRef);
        } catch (IOException e) {
            throw new LreClientException("Failed to parse JSON response", e);
        }
    }

    private String detectContentType(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            if (contentType != null && !contentType.isBlank()) {
                return contentType;
            }
        } catch (IOException ignored) {
            // Ignore and fallback
        }

        return "application/octet-stream";
    }
}