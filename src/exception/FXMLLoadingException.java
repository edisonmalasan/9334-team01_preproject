package exception;

public class FXMLLoadingException extends Exception{
    public FXMLLoadingException(String fxmlFile, Throwable cause) {
        super("Failed to load " + fxmlFile, cause);
    }
}
