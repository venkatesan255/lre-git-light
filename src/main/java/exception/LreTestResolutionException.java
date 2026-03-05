package exception;

public class LreTestResolutionException extends RuntimeException {

    public LreTestResolutionException(String message) {
        super(message);
    }

    public LreTestResolutionException(String message, Throwable cause) {
        super(message, cause);
    }

}