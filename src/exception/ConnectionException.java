package exception;
/**
 * Represents an exception when error occurs during connection
 */
public class ConnectionException extends Exception{
    public ConnectionException(String message){
        super(message);
    }
    public ConnectionException(String message, Throwable cause){
        super(message, cause);
    }
}
