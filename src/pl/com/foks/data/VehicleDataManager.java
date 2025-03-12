package pl.com.foks.data;

import pl.com.foks.exceptions.RentalException;
import pl.com.foks.repository.IRepositoryEntry;
import pl.com.foks.repository.vehicle.IVehicleRepository;
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
    public VehicleDataManager(Path vehicles) {
        if (!vehicles.toFile().exists()) {
            throw new RentalException("Rental data file does not exist");
        }
        this.vehicles = vehicles;
    }

    @Override
    public void save(List<Vehicle> vehicleList) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(vehicles.toFile(), false));
        List<String> vehiclesCSV = new ArrayList<>();
        vehicleList.forEach(vehicle -> vehiclesCSV.add(vehicle.toCSV()));
        try {
            writer.write(String.join("\n", vehiclesCSV));
            writer.flush();
        } catch (IOException e) {
            throw new RentalException("Cannot write to file", e);
        }
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
