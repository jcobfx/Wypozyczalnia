package pl.com.foks.vehicle;

import pl.com.foks.rental.User;

import java.util.Objects;

public class Motorcycle extends Vehicle {
    private User.DriversLicenseCategory category;

    public Motorcycle() {
        super(0, "", "", 0, 0, false);
    }

    private Motorcycle(int id, String brand, String model, int year, int price, boolean rented, User.DriversLicenseCategory category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }

    @Override
    public boolean canBeRented(User user) {
        return super.canBeRented(user) && User.DriversLicenseCategory.canDrive(user.getDriversLicenseCategory(), category);
    }

    @Override
    public Motorcycle build(String[] data) {
        validateData(data, 7);
        return new Motorcycle(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5]), User.DriversLicenseCategory.valueOf(data[6]));
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s", super.toCSV(), category);
    }

    @Override
    public Motorcycle clone() {
        return new Motorcycle(this.getId(), this.getBrand(), this.getModel(), this.getYear(), this.getPrice(), this.isRented(), category);
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
