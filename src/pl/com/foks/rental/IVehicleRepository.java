package pl.com.foks.rental;

import pl.com.foks.vehicle.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    boolean rentVehicle(Client client, Vehicle vehicle);
    boolean returnVehicle(Client client, Vehicle vehicle);
    List<Vehicle> getVehicles();
    void save();
    void load();
}
