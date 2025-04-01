package pl.com.foks.infrastructure.repository;

import com.google.gson.reflect.TypeToken;
import pl.com.foks.domain.Rental;
import pl.com.foks.infrastructure.utils.JsonFileStorage;
import pl.com.foks.infrastructure.utils.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class RentalRepository implements IRentalRepository {
    private final JsonFileStorage<Rental> storage;
    private final List<Rental> currentRentals;
    private final List<Rental> returnedRentals;

    public RentalRepository(String filename) {
        storage = new JsonFileStorage<>(filename, new TypeToken<ArrayList<Rental>>() {}.getType());
        currentRentals = new ArrayList<>();
        returnedRentals = new ArrayList<>();
        storage.load().forEach(r -> {
            if (r.getReturnDate() == null) {
                currentRentals.add(r);
            } else {
                returnedRentals.add(r);
            }
        });
    }

    /**
     * Finds all rentals in current and returned rentals
     * @return list of rentals
     */
    @Override
    public List<Rental> findAll() {
        return StreamUtils.concatLists(currentRentals, returnedRentals);
    }

    /**
     * Finds rental by id in current and returned rentals
     * @param id rental id
     * @return rental
     */
    @Override
    public Optional<Rental> findById(String id) {
        return Stream.concat(currentRentals.stream(), returnedRentals.stream())
                .filter(rental -> rental.getId().equals(id)).findFirst();
    }

    /**
     * Finds rental by user id in current rentals
     * @param userId user id
     * @return rental
     */
    @Override
    public Optional<Rental> findByUserId(String userId) {
        return currentRentals.stream().filter(rental -> rental.getUserId().equals(userId)).findFirst();
    }

    /**
     * Finds rental by vehicle id in current rentals
     * @param vehicleId vehicle id
     * @return rental
     */
    @Override
    public Optional<Rental> findByVehicleId(String vehicleId) {
        return currentRentals.stream().filter(rental -> rental.getVehicleId().equals(vehicleId)).findFirst();
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }
        if (rental.getReturnDate() != null) {
            returnedRentals.add(rental);
        } else {
            currentRentals.add(rental);
        }
        storage.save(StreamUtils.concatLists(currentRentals, returnedRentals));
        return rental;
    }

    @Override
    public void deleteById(String id) {
        currentRentals.removeIf(rental -> rental.getId().equals(id));
        returnedRentals.removeIf(rental -> rental.getId().equals(id));
        storage.save(StreamUtils.concatLists(currentRentals, returnedRentals));
    }
}
