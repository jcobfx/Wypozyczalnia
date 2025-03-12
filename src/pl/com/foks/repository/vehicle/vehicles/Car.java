package pl.com.foks.repository.vehicle.vehicles;

import pl.com.foks.util.DataUtils;

public class Car extends Vehicle {
    public Car() {
        super(0, "", "", 0, 0, false);
    }

    private Car(int id, String brand, String model, int year, int price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    /**
     * Builds a car object from the data
     * @param data data to build the object from
     * @return created car object
     */
    public static Car fromCSV(String[] data) {
        DataUtils.validateData(data, 6);
        return new Car(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5]));
    }

    @Override
    public Car deepClone() {
        return new Car(this.getIdentifier(), this.getBrand(), this.getModel(), this.getYear(), this.getPrice(), this.isRented());
    }
}
