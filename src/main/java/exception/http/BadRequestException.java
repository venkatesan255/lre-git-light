package exception.http;

import exception.LreClientException;

public class BadRequestException extends LreClientException {

    public BadRequestException(String message) {
        super(message, 400);
    }
}