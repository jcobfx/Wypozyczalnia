package pl.com.foks.rental;

import pl.com.foks.vehicle.Vehicle;

import java.util.List;

public interface IRental {
    boolean rentVehicle(User user, Vehicle vehicle);
    boolean returnVehicle(User user, Vehicle vehicle);
    List<Vehicle> getVehicles();
    void save();
    void load();
}
