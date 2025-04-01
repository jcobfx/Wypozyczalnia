package pl.com.foks.domain;

import lombok.AllArgsConstructor;
import pl.com.foks.infrastructure.repository.RentalRepository;

import java.util.Optional;

@AllArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;

    public Optional<Rental> getRentalByUserId(String userId) {
        return rentalRepository.findByUserId(userId);
    }

    public Optional<Rental> getRentalByVehicleId(String vehicleId) {
        return rentalRepository.findByVehicleId(vehicleId);
    }
}
