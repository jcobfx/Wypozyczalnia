package pl.com.foks.vehicle;

import pl.com.foks.rental.Client;

public class Car extends Vehicle {
    public Car() {
        super(0, "", "", 0, 0, false);
    }

    private Car(int id, String brand, String model, int year, int price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    @Override
    public boolean canBeRented(Client client) {
        return super.canBeRented(client) && Client.DriversLicenseCategory.canDrive(client.getDriversLicenseCategory(), Client.DriversLicenseCategory.B);
    }

    @Override
    public Car build(String[] data) {
        validateData(data, 6);
        return new Car(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5]));
    }
}
