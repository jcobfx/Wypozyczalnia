package pl.com.foks.rental;

import pl.com.foks.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private final String name;
    private final DriversLicenseCategory driversLicenseCategory;
    private final List<Vehicle> rentedVehicles;

    public Client(String name, DriversLicenseCategory driversLicenseCategory) {
        this.name = name;
        this.driversLicenseCategory = driversLicenseCategory;
        this.rentedVehicles = new ArrayList<>();
    }

    public DriversLicenseCategory getDriversLicenseCategory() {
        return driversLicenseCategory;
    }

    boolean hasRentedVehicle(Vehicle vehicle) {
        return rentedVehicles.contains(vehicle);
    }

    void rentVehicle(Vehicle vehicle) {
        rentedVehicles.add(vehicle);
    }

    void returnVehicle(Vehicle vehicle) {
        rentedVehicles.remove(vehicle);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", driversLicenseCategory=" + driversLicenseCategory +
                ", rentedVehicles=" + rentedVehicles +
                '}';
    }

    public enum DriversLicenseCategory {
        B, AM, A1, A2, A;

        public static boolean canDrive(DriversLicenseCategory clientCategory, DriversLicenseCategory vehicleCategory) {
            return clientCategory == B && vehicleCategory == B
                    || clientCategory == AM && (vehicleCategory == AM || vehicleCategory == B)
                    || clientCategory == A1 && (vehicleCategory == A1 || vehicleCategory == AM)
                    || clientCategory == A2 && (vehicleCategory == A2 || vehicleCategory == A1 || vehicleCategory == AM)
                    || clientCategory == A && (vehicleCategory == A || vehicleCategory == A2 || vehicleCategory == A1 || vehicleCategory == AM);
        }
    }
}
