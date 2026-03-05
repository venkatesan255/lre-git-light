package exception.http;

import exception.LreClientException;

public class ServiceUnavailableException extends LreClientException {

    public ServiceUnavailableException(String message, int status) {
        super(message, status);
    }
}