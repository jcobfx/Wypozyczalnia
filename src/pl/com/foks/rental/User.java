package pl.com.foks.rental;

import pl.com.foks.vehicle.Vehicle;

public class User {
    private final Role role;
    private final String name;
    private final DriversLicenseCategory driversLicenseCategory;
    private Vehicle rentedVehicle;

    public User(Role role, String name, DriversLicenseCategory driversLicenseCategory) {
        this.role = role;
        this.name = name;
        this.driversLicenseCategory = driversLicenseCategory;
        this.rentedVehicle = null;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public DriversLicenseCategory getDriversLicenseCategory() {
        return driversLicenseCategory;
    }

    public Vehicle getRentedVehicle() {
        return rentedVehicle;
    }

    void setRentedVehicle(Vehicle vehicle) {
        rentedVehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", driversLicenseCategory=" + driversLicenseCategory +
                ", rentedVehicle=" + rentedVehicle +
                '}';
    }

    public enum Role {
        ADMIN, CLIENT
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
