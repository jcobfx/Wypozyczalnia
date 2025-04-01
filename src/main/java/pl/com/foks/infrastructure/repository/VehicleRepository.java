package pl.com.foks.infrastructure.repository;

import com.google.gson.reflect.TypeToken;
import pl.com.foks.domain.Vehicle;
import pl.com.foks.infrastructure.utils.JsonFileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehicleRepository implements IVehicleRepository {
    private final JsonFileStorage<Vehicle> storage;
    private final List<Vehicle> vehicles;

    public VehicleRepository(String filename) {
        storage = new JsonFileStorage<>(filename, new TypeToken<ArrayList<Vehicle>>() {}.getType());
        this.vehicles = storage.load();
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicles.stream().filter(vehicle -> vehicle.getId().equals(id)).findFirst();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
        } else {
            deleteById(vehicle.getId());
        }
        vehicles.add(vehicle);
        storage.save(vehicles);
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        vehicles.removeIf(vehicle -> vehicle.getId().equals(id));
        storage.save(vehicles);
    }
}
