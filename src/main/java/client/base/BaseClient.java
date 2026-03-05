package client.base;

import exception.LreClientException;
import client.model.connection.LreConnection;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;

public abstract class BaseClient {

    protected final LreConnection connection;
    private final HttpClient httpClient;
    private final RequestLogger requestLogger;
    private final ErrorHandler errorHandler;
    private final RequestBuilder requestBuilder;

    private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(300);

    private static final HttpClient SHARED_HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    protected BaseClient(LreConnection connection) {
        this(connection, DEFAULT_REQUEST_TIMEOUT);
    }

    protected BaseClient(LreConnection connection, Duration requestTimeout) {
        this.connection = connection;
        this.httpClient = SHARED_HTTP_CLIENT;
        this.requestLogger = new RequestLogger();
        this.errorHandler = new ErrorHandler();
        this.requestBuilder = new RequestBuilder(connection, requestTimeout);
    }

    protected HttpResponse<String> get(String path) {
        HttpRequest request = requestBuilder.buildGet(path);
        return send(request);
    }

    protected HttpResponse<String> post(String path, Object body) {
        HttpRequest request = requestBuilder.buildPost(path, body);
        return send(request);
    }

    protected HttpResponse<String> put(String path, Object body) {
        HttpRequest request = requestBuilder.buildPut(path, body);
        return send(request);
    }

    protected HttpResponse<String> delete(String path) {
        HttpRequest request = requestBuilder.buildDelete(path);
        return send(request);
    }

    protected HttpResponse<String> postMultipart(String path, Path filePath, Object metadata) {
        HttpRequest request = requestBuilder.buildMultipart(path, filePath, metadata);
        return send(request);
    }

    private HttpResponse<String> send(HttpRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            requestLogger.logRequest(request);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            requestLogger.logResponse(request, response, elapsed(startTime));
            errorHandler.handleError(response, request);
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            requestLogger.logInterruption(request, elapsed(startTime));
            throw new LreClientException("HTTP call interrupted: " + request.uri().getPath(), e);
        } catch (IOException e) {
            requestLogger.logIOException(request, e, elapsed(startTime));
            throw new LreClientException(
                    String.format("HTTP call failed [%s %s] after %dms: %s",
                            request.method(), request.uri().getPath(), elapsed(startTime), e.getMessage()), e);
        }
    }

    private long elapsed(long startTime) {
        return System.currentTimeMillis() - startTime;
    }



}
