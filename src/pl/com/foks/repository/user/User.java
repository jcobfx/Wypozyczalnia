package pl.com.foks.repository.user;

import pl.com.foks.repository.IRepositoryEntry;
import pl.com.foks.repository.vehicle.vehicles.Vehicle;
import pl.com.foks.util.ValidatorUtils;

public class User implements IRepositoryEntry<User> {
    private final int id;
    private final Role role;
    private final String login;
    private final String password;
    private Vehicle rentedVehicle;

    User(int id, Role role, String login, String password) {
        this.id = id;
        this.role = role;
        this.login = login;
        this.password = password;
        this.rentedVehicle = null;
    }

    @Override
    public int getIdentifier() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Vehicle getRentedVehicle() {
        return rentedVehicle;
    }

    public void setRentedVehicle(Vehicle vehicle) {
        rentedVehicle = vehicle;
    }

    @Override
    public User deepClone() {
        return new User(id, role, login, password);
    }

    /**
     * Builds a User object from CSV data
     * @param data CSV data
     * @return User object
     */
    public static User fromCSV(String[] data) {
        ValidatorUtils.validateData(data, 4);
        return new User(Integer.parseInt(data[0]), Role.valueOf(data[1]), data[2], data[3]);
    }

    @Override
    public String toCSV() {
        return "%d;%s;%s;%s".formatted(id, role, login, password);
    }

    @Override
    public String toString() {
        return "%d. %s %s\nRented: %s".formatted(id, role, login, rentedVehicle != null ? rentedVehicle : "none");
    }

    public enum Role {
        ADMIN, CLIENT
    }
}
