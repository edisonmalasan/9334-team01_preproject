package exception;

public class LeaderboardException extends Exception {
    public LeaderboardException(String message) {
        super(message);
    }

    public LeaderboardException(String message, Throwable cause) {
        super(message, cause);
    }
}
