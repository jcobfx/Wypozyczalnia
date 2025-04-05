package com.umcsuser.carrent.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import com.umcsuser.carrent.db.JsonFileStorage;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehicleJsonRepository implements VehicleRepository {
    private final JsonFileStorage<Vehicle> storage;
    private final List<Vehicle> vehicles;

    public VehicleJsonRepository() {
        storage = new JsonFileStorage<>("vehicles.json", new TypeToken<ArrayList<Vehicle>>() {}.getType());
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
