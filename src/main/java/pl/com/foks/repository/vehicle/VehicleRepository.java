package pl.com.foks.repository.vehicle;

import pl.com.foks.data.IRepositoryDataManager;
import pl.com.foks.exceptions.FailedRepositoryLoadException;
import pl.com.foks.exceptions.FailedRepositorySaveException;
import pl.com.foks.repository.user.User;
import pl.com.foks.repository.vehicle.vehicles.Vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository {
    private final IRepositoryDataManager<Vehicle> dataManager;

    private final List<Vehicle> vehicles;

    public VehicleRepository(IRepositoryDataManager<Vehicle> dataManager) {
        vehicles = new ArrayList<>();
        this.dataManager = dataManager;

        addVehicle(VehicleFactory.createVehicle("Car;0;BMW;X5;2019;100000;false".split(";")));
        addVehicle(VehicleFactory.createVehicle("Motorcycle;1;Suzuki;GSX-R1000;2018;20000;false;A".split(";")));
    }

    @Override
    public boolean rentVehicle(User user, int vehicleId) {
        assert user != null;
        final Vehicle vehicle = getVehicle(vehicleId);
        if (vehicle != null && !vehicle.isRented() && user.getRentedVehicle() == null) {
            user.setRentedVehicle(vehicle);
            vehicle.setRented(this, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnVehicle(User user, int vehicleId) {
        assert user != null;
        final Vehicle vehicle = vehicles.get(vehicleId);
        if (vehicle != null && vehicle.isRented() && user.getRentedVehicle() == vehicle) {
            user.setRentedVehicle(null);
            vehicle.setRented(this, false);
            return true;
        }
        return false;
    }

    private Vehicle getVehicle(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getIdentifier() == id) {
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : this.vehicles) {
            vehicles.add(vehicle.deepClone());
        }
        return vehicles;
    }

    public List<Integer> getIdentifiers() {
        List<Integer> identifiers = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            identifiers.add(vehicle.getIdentifier());
        }
        return identifiers;
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        if (vehicles.stream().noneMatch(v -> v.getIdentifier() == vehicle.getIdentifier())) {
            vehicles.add(vehicle);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVehicle(int vehicleId) {
        if (vehicles.removeIf(v -> v.getIdentifier() == vehicleId)) {
            return true;
        }
        return false;
    }

    @Override
    public void save() {
        try {
            dataManager.save(vehicles);
        } catch (IOException e) {
            throw new FailedRepositorySaveException("Cannot save vehicles", e);
        }
    }

    @Override
    public void load() {
        try {
            dataManager.load().forEach(this::addVehicle);
        } catch (IOException e) {
            throw new FailedRepositoryLoadException("Cannot load vehicles", e);
        }
    }

    @Override
    public String toString() {
        return "Rental{" +
                "vehicles=" + vehicles +
                '}';
    }
}
