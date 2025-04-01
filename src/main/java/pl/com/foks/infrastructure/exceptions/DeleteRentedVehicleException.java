package pl.com.foks.infrastructure.exceptions;

public class DeleteRentedVehicleException extends RuntimeException {
    public DeleteRentedVehicleException(String message) {
        super(message);
    }
}
