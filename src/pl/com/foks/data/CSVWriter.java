package pl.com.foks.data;

import pl.com.foks.vehicle.Vehicle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class CSVWriter {
    private final BufferedWriter writer;

    public CSVWriter(Path file) throws IOException {
        writer = new BufferedWriter(new FileWriter(file.toFile(), false));
    }

    public void write(List<Vehicle> vehicles) {
        List<String> vehiclesCSV = new ArrayList<>();
        vehicles.forEach(vehicle -> vehiclesCSV.add(vehicle.toCSV()));
        try {
            writer.write(String.join("\n", vehiclesCSV));
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write to file", e);
        }
    }
}
