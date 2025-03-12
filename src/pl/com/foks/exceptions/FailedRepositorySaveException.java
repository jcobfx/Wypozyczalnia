package pl.com.foks.exceptions;

public class FailedRepositorySaveException extends RuntimeException {
    public FailedRepositorySaveException(String message) {
        super(message);
    }

    public FailedRepositorySaveException(String message, Exception e) {
        super(message, e);
    }
}
