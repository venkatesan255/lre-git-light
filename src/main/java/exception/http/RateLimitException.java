package exception.http;

import exception.LreClientException;

public class RateLimitException extends LreClientException {

    public RateLimitException(String message) {
        super(message, 429);
    }
}