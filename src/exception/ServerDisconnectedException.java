package exception;
/**
 * Represents an exception when server disconnects
 */
public class ServerDisconnectedException extends Exception {
    public ServerDisconnectedException(String message) {
        super(message);
    }
}
