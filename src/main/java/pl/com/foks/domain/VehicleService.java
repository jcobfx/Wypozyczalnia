package pl.com.foks.domain;

import lombok.AllArgsConstructor;
import pl.com.foks.infrastructure.exceptions.DeleteRentedVehicleException;
import pl.com.foks.infrastructure.repository.VehicleRepository;

@AllArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    private final RentalService rentalService;

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(String id) {
        rentalService.getRentalByVehicleId(id).ifPresentOrElse(rental -> {
            throw new DeleteRentedVehicleException("Vehicle is rented");
        }, () -> vehicleRepository.deleteById(id));
    }
}
