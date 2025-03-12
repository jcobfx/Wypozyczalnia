package pl.com.foks.repository.vehicle;

import pl.com.foks.repository.IRepository;
import pl.com.foks.repository.user.User;
import pl.com.foks.repository.vehicle.vehicles.Vehicle;

import java.util.List;

public interface IVehicleRepository extends IRepository {
    boolean rentVehicle(User user, int vehicleId);
    boolean returnVehicle(User user, int vehicleId);
    List<Vehicle> getVehicles();
    boolean addVehicle(Vehicle vehicle);
    boolean removeVehicle(int vehicleId);
}
