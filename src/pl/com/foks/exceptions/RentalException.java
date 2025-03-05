package pl.com.foks.exceptions;

public class RentalException extends RuntimeException {
    public RentalException(String message) {
        super(message);
    }

    public RentalException(String message, Exception e) {
        super(message, e);
    }
}
