package exception;
/**
 * Represents an exception when error occurs with the question bank
 */
public class QuestionBankException extends Exception {
    public QuestionBankException(String message) {
        super(message);
    }

    public QuestionBankException(String message, Throwable cause) {
        super(message, cause);
    }
}
