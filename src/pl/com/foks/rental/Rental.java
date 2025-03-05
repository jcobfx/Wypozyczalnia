package pl.com.foks.rental;

import pl.com.foks.data.RentalDataManager;
import pl.com.foks.exceptions.RentalException;
import pl.com.foks.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Rental implements IRental {
    private final RentalDataManager dataManager;

    private final List<Vehicle> vehicles;

    public Rental(RentalDataManager dataManager) {
        vehicles = new ArrayList<>();
        this.dataManager = dataManager;
    }

    @Override
    public boolean rentVehicle(User user, Vehicle vehicle) {
        assert user != null && vehicle != null;
        if (vehicles.contains(vehicle) && vehicle.canBeRented(user)) {
            user.setRentedVehicle(vehicle);
            vehicle.setRented(this, true);
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean returnVehicle(User user, Vehicle vehicle) {
        assert user != null && vehicle != null;
        if (vehicle.isRented() && user.getRentedVehicle().equals(vehicle) && vehicles.contains(vehicle)) {
            user.setRentedVehicle(null);
            vehicle.setRented(this, false);
            save();
            return true;
        }
        return false;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle : this.vehicles) {
            vehicles.add(vehicle.clone());
        }
        return vehicles;
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
