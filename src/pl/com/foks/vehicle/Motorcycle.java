package pl.com.foks.vehicle;

import pl.com.foks.rental.Client;

import java.util.List;
import java.util.Objects;

public class Motorcycle extends Vehicle {
    private Client.DriversLicenseCategory category;

    public Motorcycle() {
        super(0, "", "", 0, 0, false);
    }

    private Motorcycle(int id, String brand, String model, int year, int price, boolean rented, Client.DriversLicenseCategory category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }

    @Override
    public boolean canBeRented(Client client) {
        return super.canBeRented(client) && Client.DriversLicenseCategory.canDrive(client.getDriversLicenseCategory(), category);
    }

    @Override
    public Motorcycle build(String[] data) {
        validateData(data, 7);
        return new Motorcycle(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5]), Client.DriversLicenseCategory.valueOf(data[6]));
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s", super.toCSV(), category);
    }

    @Override
    public String toString() {
        return super.toString() + " " + category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Motorcycle that = (Motorcycle) o;
        return category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category);
    }
}
