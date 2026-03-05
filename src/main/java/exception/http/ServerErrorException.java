package exception.http;

import exception.LreClientException;

public class ServerErrorException extends LreClientException {

    public ServerErrorException(String message) {
        super(message, 500);
    }
}