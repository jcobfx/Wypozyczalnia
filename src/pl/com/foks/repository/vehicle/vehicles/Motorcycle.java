package pl.com.foks.repository.vehicle.vehicles;

import pl.com.foks.util.ValidatorUtils;

import java.util.Objects;

public class Motorcycle extends Vehicle {
    private final String category;

    public Motorcycle() {
        super(0, "", "", 0, 0, false);
        this.category = "";
    }

    private Motorcycle(int id, String brand, String model, int year, int price, boolean rented, String category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }

    /**
     * Builds a motorcycle object from the data
     * @param data data to build the object from
     * @return created motorcycle object
     */
    public static Motorcycle fromCSV(String[] data) {
        ValidatorUtils.validateData(data, 7);
        return new Motorcycle(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]),
                Integer.parseInt(data[4]), Boolean.parseBoolean(data[5]), data[6]);
    }

    @Override
    public Motorcycle deepClone() {
        return new Motorcycle(this.getIdentifier(), this.getBrand(), this.getModel(), this.getYear(),
                this.getPrice(), this.isRented(), category);
    }

    /**
     * class;id;brand;model;year;price;rented;category
     * @return CSV representation of the object
     */
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
        return Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category);
    }
}
