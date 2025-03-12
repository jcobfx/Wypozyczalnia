package pl.com.foks.exceptions;

public class FailedRepositoryLoadException extends RuntimeException {
    public FailedRepositoryLoadException(String message) {
        super(message);
    }

    public FailedRepositoryLoadException(String message, Exception e) {
        super(message, e);
    }
}
