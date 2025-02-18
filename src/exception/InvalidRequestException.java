package exception;
/**
 * Represents an exception when client request cannot be handled by server
 */
public class InvalidRequestException extends Exception {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
