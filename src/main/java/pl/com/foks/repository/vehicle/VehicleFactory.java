package pl.com.foks.repository.vehicle;

import pl.com.foks.repository.vehicle.vehicles.Vehicle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VehicleFactory {
    private static final Map<String, Class<? extends Vehicle>> vehicleClasses = new HashMap<>();;

    /**
     * Registers a vehicle class
     *
     * @param vehicleClass class to register
     */
    public static void registerVehicleClass(Class<? extends Vehicle> vehicleClass) {
        if (Vehicle.class.isAssignableFrom(vehicleClass)) {
            vehicleClasses.put(vehicleClass.getSimpleName().toLowerCase(), vehicleClass);
        }
    }

    /**
     * Creates a vehicle object from the data
     * @param data data to create the object from
     * @param <T> type of the vehicle
     * @return created vehicle object
     */
    @SuppressWarnings("unchecked")
    public static <T extends Vehicle> T createVehicle(String[] data) {
        try {
            Class<? extends Vehicle> clazz = vehicleClasses.get(data[0].toLowerCase());
            if (clazz == null) {
                throw new IllegalArgumentException("Unknown vehicle type: " + data[0]);
            }
            Method build = clazz.getMethod("fromCSV", String[].class);
            return (T) build.invoke(null, (Object) Arrays.copyOfRange(data, 1, data.length));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private VehicleFactory() {}
}
