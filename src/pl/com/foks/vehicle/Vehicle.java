package pl.com.foks.vehicle;

import pl.com.foks.rental.User;
import pl.com.foks.rental.Rental;

import java.util.Objects;

public abstract class Vehicle {
    private final int id;
    private final String brand;
    private final String model;
    private final int year;
    private final int price;
    private boolean rented;

    protected Vehicle(int id, String brand, String model, int year, int price, boolean rented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getPrice() {
        return price;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(Rental rental, boolean rented) {
        if (rental.getVehicles().contains(this)) {
            this.rented = rented;
            return;
        }
        throw new IllegalArgumentException("Vehicle is not in the rental");
    }

    public boolean canBeRented(User user) {
        return !isRented() && user.getRentedVehicle() == null;
    }

    public String toCSV() {
        return String.format("%s;%d;%s;%s;%d;%d;%b", this.getClass().getSimpleName(), id, brand, model, year, price, rented);
    }

    public abstract Vehicle build(String[] data);

    protected void validateData(String[] data, int expectedLength) {
        if (data == null || data.length != expectedLength) {
            throw new IllegalArgumentException("Invalid data");
        }
    }

    public abstract Vehicle clone();

    @Override
    public String toString() {
        return String.format("%s %s %s %d %d %b", this.getClass().getSimpleName(), brand, model, year, price, rented);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return getId() == vehicle.getId() && getYear() == vehicle.getYear() && getPrice() == vehicle.getPrice() && isRented() == vehicle.isRented() && Objects.equals(getBrand(), vehicle.getBrand()) && Objects.equals(getModel(), vehicle.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBrand(), getModel(), getYear(), getPrice(), isRented());
    }
}
