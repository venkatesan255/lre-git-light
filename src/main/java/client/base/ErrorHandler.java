package client.base;

import exception.LreClientException;
import exception.http.*;
import org.apache.commons.lang3.StringUtils;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ErrorHandler {

    public void handleError(HttpResponse<String> response, HttpRequest request) {
        int status = response.statusCode();
        if (status >= 200 && status < 300) return;

        String requestInfo = String.format("[%s %s]", request.method(), request.uri().getPath());
        String responseBody = StringUtils.truncate(response.body(), 1000);
        String errorMessage = ErrorMessageExtractor.extract(response.body());
        String message = errorMessage != null ? errorMessage : responseBody;

        throwAppropriateException(status, requestInfo, message);
    }

    private void throwAppropriateException(int status, String requestInfo, String message) {
        switch (status) {
            case 400 -> throw new BadRequestException(requestInfo + ": " + message);
            case 401 -> throw new UnauthorizedException(requestInfo + ": " + message);
            case 403 -> throw new ForbiddenException(requestInfo + ": " + message);
            case 404 -> throw new NotFoundException(requestInfo + ": " + message);
            case 429 -> throw new RateLimitException(requestInfo + ": " + message);
            case 500 -> throw new ServerErrorException(requestInfo + ": " + message);
            case 502, 503, 504 -> throw new ServiceUnavailableException(
                    String.format("%s (status: %d)", requestInfo, status), status);
            default -> throw new LreClientException(
                    String.format("Unexpected response %d %s: %s", status, requestInfo, message));
        }
    }
}