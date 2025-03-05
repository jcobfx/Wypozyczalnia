package pl.com.foks.data;

import pl.com.foks.vehicle.Vehicle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CSVReader {
    private final BufferedReader reader;

    public CSVReader(Path file) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(file.toFile()));
    }

    /**
     * Reads the file and creates a list of vehicles.
     * Vehicles are built using reflection with build methods from classes in "pl.com.foks.vehicle" package.
     * @return list of vehicles
     */
    public List<Vehicle> read() {
        final List<Vehicle> vehicles = new ArrayList<>();
        reader.lines().forEach(line -> {
            final String[] split = line.split(";");
            try {
                Class<?> clazz = Class.forName("pl.com.foks.vehicle." + split[0]);
                Method build = clazz.getMethod("build", String[].class);
                vehicles.add((Vehicle) build.invoke(clazz.getDeclaredConstructor().newInstance(),
                        (Object) Arrays.copyOfRange(split, 1, split.length)));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                     ClassNotFoundException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        });
        return vehicles;
    }
}
