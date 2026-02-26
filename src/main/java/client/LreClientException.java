package client;

public class LreClientException extends RuntimeException {

    public LreClientException(String message) {
        super(message);
    }

    public LreClientException(String message, Throwable cause) {
        super(message, cause);
    }
}