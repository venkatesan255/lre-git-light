package exception;

public class LreException extends RuntimeException {

    public LreException(String message) {
        super(message);
    }

    public LreException(String message, Throwable cause) {
        super(message, cause);
    }

    public LreException(Throwable cause) {
        super(cause);
    }
}