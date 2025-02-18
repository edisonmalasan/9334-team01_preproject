package exception;
/**
 * Represents an exception when FXML files cannot be loaded
 */
public class FXMLLoadingException extends Exception{
    public FXMLLoadingException(String fxmlFile, Throwable cause) {
        super("Failed to load " + fxmlFile, cause);
    }
}
