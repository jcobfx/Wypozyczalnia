package pl.com.foks.data;

import pl.com.foks.repository.vehicle.vehicles.Vehicle;
import pl.com.foks.repository.vehicle.VehicleFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VehicleDataManager implements IRepositoryDataManager<Vehicle> {
    private final Path vehicles;

    /**
     * Creates a new instance of the RentalDataManager
     * @param vehicles path to the file with rental data
     */
    public VehicleDataManager(Path vehicles) throws FileNotFoundException {
        if (!vehicles.toFile().exists()) {
            throw new FileNotFoundException("Rental data file does not exist");
        }
        this.vehicles = vehicles;
    }

    @Override
    public void save(List<Vehicle> vehicleList) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(vehicles.toFile(), false));
        List<String> vehiclesCSV = new ArrayList<>();
        vehicleList.forEach(vehicle -> vehiclesCSV.add(vehicle.toCSV()));
        writer.write(String.join("\n", vehiclesCSV));
        writer.flush();
    }

    @Override
    public List<Vehicle> load() throws FileNotFoundException {
        final List<Vehicle> vehicleList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(vehicles.toFile()));
        reader.lines().forEach(line -> {
            final String[] split = line.split(";");
            vehicleList.add(VehicleFactory.createVehicle(split));
        });
        return vehicleList;
    }
}
