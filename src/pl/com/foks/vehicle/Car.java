package pl.com.foks.vehicle;

import pl.com.foks.rental.User;

public class Car extends Vehicle {
    public Car() {
        super(0, "", "", 0, 0, false);
    }

    private Car(int id, String brand, String model, int year, int price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    @Override
    public boolean canBeRented(User user) {
        return super.canBeRented(user) && User.DriversLicenseCategory.canDrive(user.getDriversLicenseCategory(), User.DriversLicenseCategory.B);
    }

    @Override
    public Car build(String[] data) {
        validateData(data, 6);
        return new Car(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5]));
    }

    @Override
    public Car clone() {
        return new Car(this.getId(), this.getBrand(), this.getModel(), this.getYear(), this.getPrice(), this.isRented());
    }
}
