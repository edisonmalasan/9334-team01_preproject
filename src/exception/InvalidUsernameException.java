package exception;
/**
 * Represents an exception when username entered is not valied
 */
public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
