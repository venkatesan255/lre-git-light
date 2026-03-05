package exception.http;

import exception.LreClientException;

public class NotFoundException extends LreClientException {

    public NotFoundException(String message) {
        super(message, 404);
    }
}