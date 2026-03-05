package exception;

import lombok.Getter;

@Getter
public class LreClientException extends RuntimeException {

    private final Integer httpStatus;

    public LreClientException(String message) {
        this(message, null, null);
    }

    public LreClientException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public LreClientException(String message, Integer httpStatus) {
        this(message, null, httpStatus);
    }

    private LreClientException(String message, Throwable cause, Integer httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

}