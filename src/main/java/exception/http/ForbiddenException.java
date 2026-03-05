package exception.http;

import exception.LreClientException;

public class ForbiddenException extends LreClientException {

    public ForbiddenException(String message) {
        super(message, 403);
    }
}