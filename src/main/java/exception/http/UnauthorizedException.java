package exception.http;

import exception.LreClientException;

public class UnauthorizedException extends LreClientException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }
}