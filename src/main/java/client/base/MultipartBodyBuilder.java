package client.base;

import exception.LreClientException;
import util.serialization.JsonUtils;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class MultipartBodyBuilder {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";

    private final String boundary;

    public MultipartBodyBuilder() {
        this.boundary = "----LREBoundary" + UUID.randomUUID();
    }

    public HttpRequest.BodyPublisher build(Path filePath, Object metadata) {
        String metadataJson = JsonUtils.toJson(metadata);
        String fileName = filePath.getFileName().toString();

        String metadataPart = buildMetadataPart(metadataJson);
        String filePartHeader = buildFilePartHeader(fileName, filePath);
        String closingBoundary = "\r\n--" + boundary + "--\r\n";

        try {
            return HttpRequest.BodyPublishers.concat(
                    HttpRequest.BodyPublishers.ofString(metadataPart, StandardCharsets.UTF_8),
                    HttpRequest.BodyPublishers.ofString(filePartHeader, StandardCharsets.UTF_8),
                    HttpRequest.BodyPublishers.ofFile(filePath),
                    HttpRequest.BodyPublishers.ofString(closingBoundary, StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new LreClientException("Failed to build multipart body for file: " + filePath, e);
        }
    }

    private String buildMetadataPart(String metadataJson) {
        return "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"metadata\"\r\n" +
                CONTENT_TYPE + ": " + JSON_CONTENT_TYPE + "\r\n\r\n" +
                metadataJson + "\r\n";
    }

    private String buildFilePartHeader(String fileName, Path filePath) {
        return "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n" +
                CONTENT_TYPE + ": " + detectContentType(filePath) + "\r\n\r\n";
    }


    private String detectContentType(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            return (contentType != null && !contentType.isBlank())
                    ? contentType
                    : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    public String getContentTypeHeader() {
        return "multipart/form-data; boundary=" + boundary;
    }



}