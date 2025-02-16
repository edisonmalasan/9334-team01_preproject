package exception;

public class ThreadInterruptedException extends RuntimeException {
    public ThreadInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}