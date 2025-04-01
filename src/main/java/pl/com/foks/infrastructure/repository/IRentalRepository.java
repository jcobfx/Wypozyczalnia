package pl.com.foks.infrastructure.repository;

import pl.com.foks.domain.Rental;

import java.util.List;
import java.util.Optional;

public interface IRentalRepository {
    List<Rental> findAll();
    Optional<Rental> findById(String id);
    Optional<Rental> findByUserId(String userId);
    Optional<Rental> findByVehicleId(String vehicleId);
    Rental save(Rental rental);
    void deleteById(String id);
}
