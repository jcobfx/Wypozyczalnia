package pl.com.foks.rental;

import pl.com.foks.data.RentalDataManager;
import pl.com.foks.exceptions.RentalException;
import pl.com.foks.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Rental implements IVehicleRepository {
    private final RentalDataManager dataManager;

    private final List<Vehicle> vehicles;

    public Rental(RentalDataManager dataManager) {
        vehicles = new ArrayList<>();
        this.dataManager = dataManager;
    }

    private boolean canRentVehicle(Client client, Vehicle vehicle) {
        return vehicle.canBeRented(client);
    }

    @Override
    public boolean rentVehicle(Client client, Vehicle vehicle) {
        if (vehicles.contains(vehicle) && canRentVehicle(client, vehicle)) {
            client.rentVehicle(vehicle);
            vehicle.setRented(this, true);
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean returnVehicle(Client client, Vehicle vehicle) {
        if (vehicles.contains(vehicle) && vehicle.isRented() && client.hasRentedVehicle(vehicle)) {
            client.returnVehicle(vehicle);
            vehicle.setRented(this, false);
            save();
            return true;
        }
        return false;
    }

    @Override
    public List<Vehicle> getVehicles() {
        return List.copyOf(vehicles);
    }

    @Override
    public void save() {
        try {
            dataManager.save(vehicles);
        } catch (Exception e) {
            throw new RentalException("Cannot save rental", e);
        }
    }

    @Override
    public void load() {
        try {
            dataManager.load(vehicles);
        } catch (Exception e) {
            throw new RentalException("Cannot load rental", e);
        }
    }

    @Override
    public String toString() {
        return "Rental{" +
                "vehicles=" + vehicles +
                '}';
    }
}
