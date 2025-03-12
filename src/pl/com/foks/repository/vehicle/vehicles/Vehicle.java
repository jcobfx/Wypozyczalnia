package pl.com.foks.repository.vehicle.vehicles;

import pl.com.foks.repository.IRepositoryEntry;
import pl.com.foks.repository.vehicle.VehicleRepository;

import java.util.Objects;

public abstract class Vehicle implements IRepositoryEntry<Vehicle> {
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

    /**
     * Build method has to be overloaded in subclasses
     * @throws UnsupportedOperationException when the method is used from the superclass
     */
    public static Vehicle fromCSV(String[] ignoredData) {
        throw new UnsupportedOperationException("Build not implemented");
    }

    @Override
    public int getIdentifier() {
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

    public void setRented(VehicleRepository rental, boolean rented) {
        if (rental.getVehicles().contains(this)) {
            this.rented = rented;
            return;
        }
        throw new IllegalArgumentException("Vehicle is not in the rental");
    }

    /**
     * class;id;brand;model;year;price;rented
     * @return CSV representation of the object
     */
    @Override
    public String toCSV() {
        return String.format("%s;%d;%s;%s;%d;%d;%b", this.getClass().getSimpleName(), id, brand, model, year, price, rented);
    }

    @Override
    public String toString() {
        return String.format("%d. %s %s %s %d %d %b", id, this.getClass().getSimpleName(), brand, model, year, price, rented);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return getIdentifier() == vehicle.getIdentifier() && getYear() == vehicle.getYear() && getPrice() == vehicle.getPrice() && isRented() == vehicle.isRented() && Objects.equals(getBrand(), vehicle.getBrand()) && Objects.equals(getModel(), vehicle.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getBrand(), getModel(), getYear(), getPrice(), isRented());
    }
}
